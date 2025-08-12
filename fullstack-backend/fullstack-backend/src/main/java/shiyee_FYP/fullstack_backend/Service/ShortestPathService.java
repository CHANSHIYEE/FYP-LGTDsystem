package shiyee_FYP.fullstack_backend.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shiyee_FYP.fullstack_backend.model.AmapRouteResponse;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.model.ShortestPath;
import shiyee_FYP.fullstack_backend.repository.LocationRepository;
import shiyee_FYP.fullstack_backend.repository.ShortestPathRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortestPathService {
    private final LocationRepository locationRepository;
    private final ShortestPathRepository shortestPathRepository;
    private final AmapService amapService;

    public ShortestPathService(LocationRepository locationRepository,
                               ShortestPathRepository shortestPathRepository,
                               AmapService amapService) {
        this.locationRepository = locationRepository;
        this.shortestPathRepository = shortestPathRepository;
        this.amapService = amapService;
    }

    /**
     * 计算并存储所有位置之间的最短路径
     */
    @Transactional
    public void calculateAllShortestPaths() {
        List<Location> allLocations = locationRepository.findAll();

        // 遍历所有位置组合
        for (Location origin : allLocations) {
            for (Location destination : allLocations) {
                if (!origin.getId().equals(destination.getId())) {
                    // 检查是否已存在该路径
                    if (!shortestPathRepository.existsByOriginIdAndDestinationId(origin.getId(), destination.getId())) {
                        calculateAndSaveShortestPath(origin, destination);
                    }
                }
            }
        }
    }
    public ShortestPath getShortestPath(Integer originId, Integer destinationId) {
        return shortestPathRepository.findByOriginIdAndDestinationId(originId, destinationId)
                .orElseThrow(() -> new RuntimeException(
                        "Shortest path not found between " + originId + " and " + destinationId));
    }

    /**
     * 计算两个位置之间的最短路径并保存
     */
    private void calculateAndSaveShortestPath(Location origin, Location destination) {
        String originCoord = origin.getLongitude() + "," + origin.getLatitude();
        String destinationCoord = destination.getLongitude() + "," + destination.getLatitude();

        AmapRouteResponse response = amapService.getDrivingRoute(originCoord, destinationCoord);

        if (response.getStatus() == 1 && response.getRoute() != null && !response.getRoute().getPaths().isEmpty()) {
            AmapRouteResponse.Path path = response.getRoute().getPaths().get(0);

            // 提取所有步骤的坐标点
            String pathCoordinates = path.getSteps().stream()
                    .map(AmapRouteResponse.Step::getPolyline)
                    .collect(Collectors.joining(";"));

            ShortestPath shortestPath = new ShortestPath();
            shortestPath.setOriginId(origin.getId());
            shortestPath.setDestinationId(destination.getId());
            shortestPath.setDistance(path.getDistance());
            shortestPath.setDuration(path.getDuration());
            shortestPath.setPathCoordinates(pathCoordinates);

            shortestPathRepository.save(shortestPath);
        }
    }

    /**
     * 计算指定位置与其他所有位置的最短路径
     */
    @Transactional
    public void calculateShortestPathsForLocation(Integer locationId) {
        Location origin = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));

        List<Location> otherLocations = locationRepository.findAll().stream()
                .filter(l -> !l.getId().equals(locationId))
                .collect(Collectors.toList());

        for (Location destination : otherLocations) {
            // 检查是否已存在该路径
            if (!shortestPathRepository.existsByOriginIdAndDestinationId(origin.getId(), destination.getId())) {
                calculateAndSaveShortestPath(origin, destination);
            }
        }
    }


}