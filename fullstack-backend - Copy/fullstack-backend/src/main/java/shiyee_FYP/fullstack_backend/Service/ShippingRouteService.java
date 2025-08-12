package shiyee_FYP.fullstack_backend.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.exception.NoPortFoundException;
import shiyee_FYP.fullstack_backend.model.DTO.PortDistanceDTO;
import shiyee_FYP.fullstack_backend.model.DTO.ShippingRoute;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.Port;
import shiyee_FYP.fullstack_backend.model.ShippingLane;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;
import shiyee_FYP.fullstack_backend.repository.LocationRepository;
import shiyee_FYP.fullstack_backend.repository.PortRepository;
import shiyee_FYP.fullstack_backend.repository.ShippingLaneRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class ShippingRouteService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingRouteService.class);
    private static final Logger log = LoggerFactory.getLogger(ShippingRouteService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GlobalLocationRepository globalLocationRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private ShippingLaneRepository shippingLaneRepository;

    @Autowired
    private ShippingLaneService shippingLaneService;

    @Autowired
    private GoogleMapsService googleMapsService;

    @Value("${global.google.maps.api.key}")
    private String googleMapsApiKey;

    double searchRadius = 200.0;

    private static final double DEFAULT_SEARCH_RADIUS = 200;



    public List<PortDistanceDTO> getNearestPortsByLocationId(Long locationId, Double maxDistance) {
        // 查找地点信息
        GlobalLocation location = globalLocationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));

        // 使用提供的距离或默认距离
        double searchRadius = maxDistance != null ? maxDistance : DEFAULT_SEARCH_RADIUS;

        logger.info("Searching for ports within {} km of location {} (lat: {}, lon: {})",
                searchRadius, locationId, location.getLatitude(), location.getLongitude());

        return getNearestPorts(location.getLatitude(), location.getLongitude(), searchRadius);
    }

    public List<PortDistanceDTO> getNearestPorts(double latitude, double longitude, double maxDistance) {
        List<Object[]> results = portRepository.findNearbyPortsWithDistance(latitude, longitude, maxDistance);

        return results.stream()
                .map(result -> {
                    Port port = new Port();
                    port.setId(((Number) result[0]).longValue());
                    port.setName((String) result[1]);
                    port.setCountry((String) result[2]);
                    port.setLatitude((Double) result[3]);
                    port.setLongitude((Double) result[4]);
                    Double distance = (Double) result[5];

                    return new PortDistanceDTO(port, distance);
                })
                .collect(Collectors.toList());
    }

public ShippingRoute getShippingRoute(Long sourceLocationId, Long targetLocationId) {
    // 1. 获取位置信息
    GlobalLocation sourceLocation = globalLocationRepository.findById(sourceLocationId)
            .orElseThrow(() -> new EntityNotFoundException("Source location not found"));
    GlobalLocation targetLocation = globalLocationRepository.findById(targetLocationId)
            .orElseThrow(() -> new EntityNotFoundException("Target location not found"));

    // 2. 查找最近的港口
    List<PortDistanceDTO> sourcePorts = getNearestPortsByLocationId(sourceLocationId, DEFAULT_SEARCH_RADIUS);
    List<PortDistanceDTO> targetPorts = getNearestPortsByLocationId(targetLocationId, DEFAULT_SEARCH_RADIUS);

    if (sourcePorts.isEmpty() || targetPorts.isEmpty()) {
        throw new NoPortFoundException("No nearby ports found within " + DEFAULT_SEARCH_RADIUS + "km radius");
    }

    Port sourcePort = sourcePorts.get(0).getPort();
    Port targetPort = targetPorts.get(0).getPort();

    // 3. 获取Google Maps路线
    List<String> directionsToPort = getDirectionsWithFallback(
            sourceLocation, sourcePort, "Source location to port");
    List<String> directionsFromPort = getDirectionsWithFallback(
            targetPort, targetLocation, "Port to target location");

    // 4. 构建返回对象
    ShippingRoute route = new ShippingRoute();
    route.setSourceLocation(sourceLocation);
    route.setSourcePort(sourcePort);
    route.setTargetPort(targetPort);
    route.setTargetLocation(targetLocation);
    route.setLandDirectionsToSourcePort(directionsToPort);
    route.setLandDirectionsFromTargetPort(directionsFromPort);

    // 5. 处理航运信息（没有ShippingLane时生成默认信息）
    processShippingInfo(route, sourcePort, targetPort);

    return route;
}

    private List<String> getDirectionsWithFallback(GlobalLocation from, Port to, String routeName) {
        try {
            return googleMapsService.getDirections(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude());
        } catch (Exception e) {
            log.warn("Failed to get directions for {}: {}", routeName, e.getMessage());
            return Collections.singletonList("Directions unavailable for " + routeName);
        }
    }

    private List<String> getDirectionsWithFallback(Port from, GlobalLocation to, String routeName) {
        try {
            return googleMapsService.getDirections(
                    from.getLatitude(), from.getLongitude(),
                    to.getLatitude(), to.getLongitude());
        } catch (Exception e) {
            log.warn("Failed to get directions for {}: {}", routeName, e.getMessage());
            return Collections.singletonList("Directions unavailable for " + routeName);
        }
    }

    private void processShippingInfo(ShippingRoute route, Port sourcePort, Port targetPort) {
        // 尝试获取现有航线数据
        Optional<ShippingLane> laneOpt = shippingLaneService.findBestShippingLane(
                sourcePort.getId(),
                targetPort.getId()
        );

        if (laneOpt.isPresent()) {
            ShippingLane lane = laneOpt.get();
            route.setShippingLane(lane);

            // 处理中转港口
            if (lane.getViaPorts() != null && !lane.getViaPorts().isEmpty()) {
                List<Port> viaPorts = Arrays.stream(lane.getViaPorts().split(","))
                        .map(Long::parseLong)
                        .map(portRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                route.setViaPorts(viaPorts);
            }

            route.setShippingDescription(generateShippingDescription(lane, route.getViaPorts()));
        } else {
            // 生成默认航运信息
            ShippingLane defaultLane = generateDefaultLane(sourcePort, targetPort);
            route.setShippingLane(defaultLane);
            route.setShippingDescription(generateDefaultDescription(sourcePort, targetPort));
        }
    }
    private String generateShippingDescription(ShippingLane lane, List<Port> viaPorts) {
        StringBuilder description = new StringBuilder();
        description.append("海运航线：")
                .append(lane.getStartPort().getName())
                .append(" → ");
        if (viaPorts != null && !viaPorts.isEmpty()) {
            for (Port port : viaPorts) {
                description.append(port.getName()).append(" → ");
            }
        }
        description.append(lane.getEndPort().getName());

        description.append("，总距离约").append(String.format("%.1f", lane.getDistance())).append("公里");
        description.append("，预计耗时").append(lane.getEstimatedTime()).append("天");

        return description.toString();
    }


    private ShippingLane generateDefaultLane(Port sourcePort, Port targetPort) {
        ShippingLane lane = new ShippingLane();
        lane.setStartPort(sourcePort);
        lane.setEndPort(targetPort);

        // 计算大圆距离
        double distance = calculateGreatCircleDistance(
                sourcePort.getLatitude(), sourcePort.getLongitude(),
                targetPort.getLatitude(), targetPort.getLongitude()
        );
        lane.setDistance(distance);

        // 估算时间（假设平均航速500公里/天）
        lane.setEstimatedTime((int) Math.max(1, Math.ceil(distance / 500)));

        return lane;
    }

    private String generateDefaultDescription(Port sourcePort, Port targetPort) {
        double distance = calculateGreatCircleDistance(
                sourcePort.getLatitude(), sourcePort.getLongitude(),
                targetPort.getLatitude(), targetPort.getLongitude()
        );
        int days = (int) Math.max(1, Math.ceil(distance / 500));

        return String.format("海运航线：%s → %s，直线距离约%.1f公里，预计%d天",
                sourcePort.getName(),
                targetPort.getName(),
                distance,
                days);
    }

    private double calculateGreatCircleDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 地球半径(km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
