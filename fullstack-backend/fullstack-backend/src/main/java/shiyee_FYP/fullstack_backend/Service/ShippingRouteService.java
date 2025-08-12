package shiyee_FYP.fullstack_backend.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class ShippingRouteService {
//
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//    private final GlobalLocationRelationRepository relationRepository;
//    private final GlobalLocationRepository locationRepository;
//
//    @Value("${google.maps.api.key}")
//    private String googleApiKey;
//
//    @Value("${searoutes.api.key}")
//    private String searoutesApiKey;
//
//    public Map<String, Object> getFullShippingRoute(Long relationId) {
//        Map<String, Object> result = new LinkedHashMap<>();
//        try {
//            // 1. 验证关系记录
//            GlobalLocationRelation relation = validateRelation(relationId, "SHIPPING");
//
//            // 2. 获取地点信息
//            // 3. 获取起点和终点坐标 - 通过ID查询位置
//            GlobalLocation origin = locationRepository.findById(relation.getSourceId())
//                    .orElseThrow(() -> new EntityNotFoundException("Source location not found with id: " + relation.getSourceId()));
//
//            GlobalLocation destination = locationRepository.findById(relation.getTargetId())
//                    .orElseThrow(() -> new EntityNotFoundException("Target location not found with id: " + relation.getTargetId()));
//
//            result.put("relationId", relationId);
//            result.put("origin", buildLocationInfo(origin));
//            result.put("destination", buildLocationInfo(destination));
//
//            // 3. 获取港口信息
//            PortInfo originPort = getClosestPort(origin.getLatitude(), origin.getLongitude());
//            PortInfo destPort = getClosestPort(destination.getLatitude(), destination.getLongitude());
//            result.put("originPort", buildPortInfo(originPort));
//            result.put("destinationPort", buildPortInfo(destPort));
//
//            // 4. 获取各段路线
//            result.put("landRouteToOriginPort", getLandRoute(
//                    origin.getLatitude(), origin.getLongitude(),
//                    originPort.getLatitude(), originPort.getLongitude()));
//
//            result.put("seaRoute", getSeaRoute(originPort.getLocode(), destPort.getLocode()));
//
//            result.put("landRouteFromDestPort", getLandRoute(
//                    destPort.getLatitude(), destPort.getLongitude(),
//                    destination.getLatitude(), destination.getLongitude()));
//
//            // 5. 计算总指标
//            calculateTotalMetrics(result);
//
//        } catch (EntityNotFoundException e) {
//            result.put("error", e.getMessage());
//        } catch (Exception e) {
//            log.error("Shipping route calculation failed", e);
//            result.put("error", "Failed to calculate shipping route: " + e.getMessage());
//        }
//        return result;
//    }
//
//    // === 内部方法 ===
//    private GlobalLocationRelation validateRelation(Long relationId, String expectedMode) {
//        GlobalLocationRelation relation = relationRepository.findById(relationId)
//                .orElseThrow(() -> new EntityNotFoundException("Relation not found with id: " + relationId));
//
//        if (!expectedMode.equalsIgnoreCase(relation.getTransportMode())) {
//            throw new IllegalArgumentException("Only " + expectedMode + " transport mode supported");
//        }
//        return relation;
//    }
//
//    private GlobalLocation getLocationById(Integer locationId) {
//        return locationRepository.findById(Long.valueOf(locationId))
//                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + locationId));
//    }
//
//    private PortInfo getClosestPort(double lat, double lng) throws JsonProcessingException {
//        String url = String.format(
//                "https://api.searoutes.com/geocoding/v2/closest/%f,%f?locationTypes=port",
//                lng, lat);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("x-api-key", searoutesApiKey);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
//
//        JsonNode root = objectMapper.readTree(response.getBody());
//        JsonNode port = root.path("locations").get(0);
//
//        return new PortInfo(
//                port.path("locode").asText(),
//                port.path("name").asText(),
//                port.path("geometry").path("coordinates").get(1).asDouble(),
//                port.path("geometry").path("coordinates").get(0).asDouble()
//        );
//    }
//
//    private Map<String, Object> getLandRoute(double originLat, double originLng,
//                                             double destLat, double destLng) {
//        try {
//            String url = String.format(
//                    "https://maps.googleapis.com/maps/api/directions/json?" +
//                            "origin=%f,%f&destination=%f,%f&key=%s",
//                    originLat, originLng, destLat, destLng, googleApiKey);
//
//            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//            return objectMapper.readValue(response.getBody(), Map.class);
//        } catch (Exception e) {
//            return Map.of("error", "Land route error: " + e.getMessage());
//        }
//    }
//
//    private Map<String, Object> getSeaRoute(String originLocode, String destLocode) {
//        try {
//            String url = String.format(
//                    "https://api.searoutes.com/route/v2/sea/%s;%s?vesselDraft=5&allowIceAreas=false",
//                    originLocode, destLocode);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("x-api-key", searoutesApiKey);
//            headers.set("accept-version", "2.0");
//
//            ResponseEntity<String> response = restTemplate.exchange(
//                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
//
//            return objectMapper.readValue(response.getBody(), Map.class);
//        } catch (HttpClientErrorException e) {
//            return Map.of("error", "Sea route API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
//        } catch (Exception e) {
//            return Map.of("error", "Sea route error: " + e.getMessage());
//        }
//    }
//
//    private void calculateTotalMetrics(Map<String, Object> result) {
//        try {
//            double totalDistance = 0;
//            double totalDuration = 0;
//
//            // 陆路段1（起点->起运港）
//            totalDistance += extractDistance(result.get("landRouteToOriginPort"));
//            totalDuration += extractDuration(result.get("landRouteToOriginPort"));
//
//            // 海运段
//            totalDistance += extractDistance(result.get("seaRoute"));
//            totalDuration += extractDuration(result.get("seaRoute"));
//
//            // 陆路段2（目的港->终点）
//            totalDistance += extractDistance(result.get("landRouteFromDestPort"));
//            totalDuration += extractDuration(result.get("landRouteFromDestPort"));
//
//            DecimalFormat df = new DecimalFormat("#.##");
//            result.put("totalDistanceKm", Double.valueOf(df.format(totalDistance)));
//            result.put("totalDurationHours", Double.valueOf(df.format(totalDuration / 3600)));
//
//        } catch (Exception e) {
//            log.error("Metric calculation failed", e);
//            result.put("totalDistanceKm", -1);
//            result.put("totalDurationHours", -1);
//        }
//    }
//
//    private double extractDistance(Object routeData) {
//        try {
//            if (!(routeData instanceof Map)) return 0;
//
//            JsonNode node = objectMapper.valueToTree(routeData);
//            if (((Map<?, ?>) routeData).containsKey("routes")) { // Google Maps格式
//                return node.path("routes").get(0).path("legs").get(0)
//                        .path("distance").path("value").asDouble() / 1000;
//            } else { // Searoutes格式
//                return node.path("distance").path("value").asDouble() / 1000;
//            }
//        } catch (Exception e) {
//            log.warn("Distance extraction failed", e);
//            return 0;
//        }
//    }
//
//    private double extractDuration(Object routeData) {
//        try {
//            if (!(routeData instanceof Map)) return 0;
//
//            JsonNode node = objectMapper.valueToTree(routeData);
//            if (((Map<?, ?>) routeData).containsKey("routes")) { // Google Maps格式
//                return node.path("routes").get(0).path("legs").get(0)
//                        .path("duration").path("value").asDouble();
//            } else { // Searoutes格式
//                return node.path("duration").path("value").asDouble();
//            }
//        } catch (Exception e) {
//            log.warn("Duration extraction failed", e);
//            return 0;
//        }
//    }
//
//    // === DTO类 ===
//    @RequiredArgsConstructor
//    @Getter
//    private static class PortInfo {
//        private final String locode;
//        private final String name;
//        private final double latitude;
//        private final double longitude;
//    }
//
//    private Map<String, Object> buildLocationInfo(GlobalLocation loc) {
//        return Map.of(
//                "id", loc.getId(),
//                "name", loc.getName(),
//                "coordinates", List.of(loc.getLatitude(), loc.getLongitude())
//        );
//    }
//
//    private Map<String, Object> buildPortInfo(PortInfo port) {
//        return Map.of(
//                "locode", port.getLocode(),
//                "name", port.getName(),
//                "coordinates", List.of(port.getLatitude(), port.getLongitude())
//        );
//    }}


import shiyee_FYP.fullstack_backend.model.DTO.PortInfo;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.*;

@Service
@Slf4j

public class ShippingRouteService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GlobalLocationRelationRepository relationRepository;
    private final GlobalLocationRepository locationRepository;
    private final PathCacheService pathCacheService;

    @Value("${google.maps.api.key}")
    private String googleApiKey;

    @Value("${searoute.api.key}")
    private String searoutesApiKey;

    @Value("${aviationstack.api.key}")
    private String aviationStackApiKey;

    public ShippingRouteService(RestTemplate restTemplate,
                                ObjectMapper objectMapper,
                                GlobalLocationRelationRepository relationRepository,
                                GlobalLocationRepository locationRepository, PathCacheService pathCacheService) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.relationRepository = relationRepository;
        this.locationRepository = locationRepository;
        this.pathCacheService = pathCacheService;
    }

    // 主方法
    public Map<String, Object> getFullShippingRoute(Long relationId) {
        // 1. 先检查缓存
        Map<String, Object> cachedResult = pathCacheService.getPathCache(relationId);
        if (cachedResult != null) {
            log.info("从缓存返回海运路线数据，relationId: {}", relationId);
            return cachedResult;
        }
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            GlobalLocationRelation relation = validateRelation(relationId, "SHIPPING");

            GlobalLocation origin = locationRepository.findById(relation.getSourceId())
                    .orElseThrow(() -> new EntityNotFoundException("源地点未找到"));

            GlobalLocation target = locationRepository.findById(relation.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("目标地点未找到"));

            // 获取最近港口
            PortInfo originPort = getClosestPort(origin.getLatitude(), origin.getLongitude());
            PortInfo destPort = getClosestPort(target.getLatitude(), target.getLongitude());

            // 获取三段路线
            Map<String, Object> landRouteToPort = getLandRoute(
                    origin.getLatitude(), origin.getLongitude(),
                    originPort.getLatitude(), originPort.getLongitude());

            Map<String, Object> seaRoute = getSeaRoute(originPort.getLocode(), destPort.getLocode());

            Map<String, Object> landRouteFromPort = getLandRoute(
                    destPort.getLatitude(), destPort.getLongitude(),
                    target.getLatitude(), target.getLongitude());
            log.debug("landRouteToPort 数据结构: {}", objectMapper.writeValueAsString(landRouteToPort));
            log.debug("seaRoute 数据结构: {}", objectMapper.writeValueAsString(seaRoute));
            log.debug("landRouteFromPort 数据结构: {}", objectMapper.writeValueAsString(landRouteFromPort));

            // 计算总指标
            double totalDistance = 0;
            double totalDuration = 0;

            totalDistance += extractDistance(landRouteToPort);
            totalDuration += extractDuration(landRouteToPort);

            totalDistance += extractDistance(seaRoute);
            totalDuration += extractDuration(seaRoute);

            totalDistance += extractDistance(landRouteFromPort);
            totalDuration += extractDuration(landRouteFromPort);

            // 构建响应
            result.put("relationId", relationId);
            result.put("transportMode", "SHIPPING");
            result.put("totalDistanceKm", totalDistance / 1000); // 转为公里
            result.put("totalDurationHours", totalDuration / 3600); // 转为小时
            result.put("origin", buildLocationInfo(origin));
            result.put("destination", buildLocationInfo(target));
            result.put("originPort", buildPortInfo(originPort));
            result.put("destinationPort", buildPortInfo(destPort));
            result.put("landRouteToOriginPort", landRouteToPort);
            result.put("seaRoute", seaRoute);
            result.put("landRouteFromDestPort", landRouteFromPort);

            // 在保存缓存前添加日志
            log.info("准备保存海运路线缓存，relationId: {}, 数据大小: {}",
                    relationId,
                    objectMapper.writeValueAsString(result).length());

            // 修正这里：使用result而不是resultMap
            pathCacheService.savePathCache(
                    relationId,
                    "SHIPPING",
                    origin.getId(),
                    target.getId(),
                    origin.getName(),
                    target.getName(),
                    result  // 这里改为使用result变量
            );

            log.info("海运路线缓存保存完成");
        } catch (Exception e) {
            log.error("海运路线缓存保存失败", e);
            result.put("error", e.getMessage());
        }
        return result;
    }

    // 提取距离的辅助方法（单位：米）
    public double extractDistance(Object routeData) throws Exception {
        String rawData = objectMapper.writeValueAsString(routeData);
        log.debug("尝试提取距离的原始数据: {}", rawData);

        try {
            JsonNode node = objectMapper.readTree(rawData);

            if (node.has("distance") && node.get("distance").has("value")) {
                return node.get("distance").get("value").asDouble();
            } else if (node.has("routes")) {
                JsonNode legs = node.path("routes").get(0).path("legs");
                if (legs.size() > 0) {
                    return legs.get(0).path("distance").path("value").asDouble();
                }
            } else if (node.has("distance") && node.get("distance").isNumber()) {
                return node.get("distance").asDouble();
            }
            // 🔽 添加这段以兼容 Searoutes
            else if (node.has("features")) {
                JsonNode features = node.path("features");
                if (features.size() > 0) {
                    return features.get(0).path("properties").path("distance").asDouble();
                }
            }

            throw new Exception("无法识别的路线数据格式。原始数据: " + rawData);
        } catch (Exception e) {
            throw new Exception("距离提取失败，原始数据: " + rawData + "，错误: " + e.getMessage());
        }
    }



    // === 核心路由方法 ===
    private Map<String, Object> getLandRoute(double originLat, double originLng,
                                             double destLat, double destLng) {
        try {
            String url = String.format(
                    "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=%f,%f&destination=%f,%f&key=%s",
                    originLat, originLng, destLat, destLng, googleApiKey);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return objectMapper.readValue(response.getBody(), HashMap.class);
        } catch (Exception e) {
            return Collections.singletonMap("error", "Land route error: " + e.getMessage());
        }
    }

    private Map<String, Object> getSeaRoute(String originLocode, String destLocode) {
        try {
            String url = String.format(
                    "https://api.searoutes.com/route/v2/sea/%s;%s?vesselDraft=5&allowIceAreas=false",
                    originLocode, destLocode);

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", searoutesApiKey);
            headers.set("accept-version", "2.0");

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            return objectMapper.readValue(response.getBody(), HashMap.class);
        } catch (Exception e) {
            return Collections.singletonMap("error", "Sea route error: " + e.getMessage());
        }
    }

private PortInfo getClosestPort(double latitude, double longitude) throws Exception {
    // 1. 验证坐标范围
    if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
        throw new IllegalArgumentException("Coordinates must be: longitude (-180 to 180), latitude (-90 to 90)");
    }

    // 3. 构建URL（使用URL编码）
    String url = String.format(
            "https://api.searoutes.com/geocoding/v2/closest/%f,%f?radius=%d&locationTypes=port",
            longitude, latitude, 200_000);

    // 4. 设置完整的请求头
    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key", searoutesApiKey);
    headers.set("accept-version", "2.0"); // 关键头
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    // 5. 发送请求
    ResponseEntity<String> response = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

    // 6. 处理响应
    // 修正后的响应处理
    JsonNode root = objectMapper.readTree(response.getBody());

// 检查 features 数组
    if (!root.has("features") || root.path("features").isEmpty()) {
        throw new RuntimeException("No port found within " + 20 + " km");
    }

// 提取第一个港口
    JsonNode feature = root.path("features").get(0);
    JsonNode props = feature.path("properties");
    JsonNode geometry = feature.path("geometry");

    return new PortInfo(
            props.path("locode").asText(),
            props.path("name").asText(),
            geometry.path("coordinates").get(1).asDouble(), // 纬度
            geometry.path("coordinates").get(0).asDouble()  // 经度
    );
}


    private void calculateTotalMetrics(Map<String, Object> result) {
        try {
            double totalDistance = 0;
            double totalDuration = 0;

            // 计算三段路线的总和
            totalDistance += extractDistance(result.get("landRouteToOriginPort"));
            totalDuration += extractDuration(result.get("landRouteToOriginPort"));

            totalDistance += extractDistance(result.get("seaRoute"));
            totalDuration += extractDuration(result.get("seaRoute"));

            totalDistance += extractDistance(result.get("landRouteFromDestPort"));
            totalDuration += extractDuration(result.get("landRouteFromDestPort"));

            // 格式化结果
            DecimalFormat df = new DecimalFormat("#.##");
            result.put("totalDistanceKm", Double.valueOf(df.format(totalDistance)));
            result.put("totalDurationHours", Double.valueOf(df.format(totalDuration / 3600)));

        } catch (Exception e) {
            result.put("totalDistanceKm", -1);
            result.put("totalDurationHours", -1);
        }
    }



    // 提取时间的辅助方法（单位：秒）
    public double extractDuration(Object routeData) throws Exception {
        if (routeData == null) {
            throw new Exception("路线数据为空");
        }

        try {
            JsonNode node = objectMapper.valueToTree(routeData);

            if (node.has("routes") && node.get("routes").size() > 0) {
                JsonNode legs = node.get("routes").get(0).get("legs");
                if (legs.size() > 0) {
                    return legs.get(0).get("duration").get("value").asDouble();
                }
            } else if (node.has("duration")) {
                JsonNode duration = node.get("duration");
                if (duration.has("value")) {
                    return duration.get("value").asDouble();
                }
                return duration.asDouble();
            }
            // 🔽 添加这段以兼容 Searoutes
            else if (node.has("features")) {
                JsonNode features = node.path("features");
                if (features.size() > 0) {
                    return features.get(0).path("properties").path("duration").asDouble();
                }
            }

            throw new Exception("无法识别的路线数据格式");
        } catch (Exception e) {
            throw new Exception("时间提取失败: " + e.getMessage());
        }
    }


    // === 工具方法 ===
    private GlobalLocationRelation validateRelation(Long relationId, String transportMode) {
        GlobalLocationRelation relation = relationRepository.findById(relationId)
                .orElseThrow(() -> new EntityNotFoundException("Relation not found"));

        if (!transportMode.equalsIgnoreCase(relation.getTransportMode())) {
            throw new IllegalArgumentException("Only " + transportMode + " mode supported");
        }
        return relation;
    }

    private GlobalLocation getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
    }

    private Map<String, Object> buildLocationInfo(GlobalLocation loc) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", loc.getId());
        map.put("name", loc.getName());
        map.put("coordinates", Arrays.asList(loc.getLatitude(), loc.getLongitude()));
        return map;
    }

    private Map<String, Object> buildPortInfo(PortInfo port) {
        Map<String, Object> map = new HashMap<>();
        map.put("locode", port.getLocode());
        map.put("name", port.getName());
        map.put("coordinates", Arrays.asList(port.getLatitude(), port.getLongitude()));
        return map;
    }
    // 在ShippingRouteService中添加以下方法

    private Map<String, Object> getAirRoute(String originIata, String destIata) {
        try {
            // 使用AviationStack API获取航班路线信息
            String url = String.format(
                    "http://api.aviationstack.com/v1/routes?access_key=%s&dep_iata=%s&arr_iata=%s",
                    aviationStackApiKey, originIata, destIata);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return objectMapper.readValue(response.getBody(), HashMap.class);
        } catch (Exception e) {
            return Collections.singletonMap("error", "Air route error: " + e.getMessage());
        }
    }

    private AirportInfo getClosestAirport(double latitude, double longitude) throws Exception {
        // 验证坐标范围
        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Coordinates must be: longitude (-180 to 180), latitude (-90 to 90)");
        }

        // 使用Searoutes API查找最近的机场
        String url = String.format(
                "https://api.searoutes.com/geocoding/v2/closest/%f,%f?radius=%d&locationTypes=airport",
                longitude, latitude, 200_000);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", searoutesApiKey);
        headers.set("accept-version", "2.0");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JsonNode root = objectMapper.readTree(response.getBody());

        if (!root.has("features") || root.path("features").isEmpty()) {
            throw new RuntimeException("No airport found within " + 200 + " km");
        }

        // 提取第一个机场
        JsonNode feature = root.path("features").get(0);
        JsonNode props = feature.path("properties");
        JsonNode geometry = feature.path("geometry");

        return new AirportInfo(
                props.path("iata").asText(), // IATA代码
                props.path("name").asText(),
                geometry.path("coordinates").get(1).asDouble(), // 纬度
                geometry.path("coordinates").get(0).asDouble()  // 经度
        );
    }

    // 添加AirportInfo类
    @Getter
    @AllArgsConstructor
    private static class AirportInfo {
        private final String iata;
        private final String name;
        private final double latitude;
        private final double longitude;
    }

    public Map<String, Object> getFullAirRoute(Long relationId) {
        Map<String, Object> cachedResult = pathCacheService.getPathCache(relationId);
        if (cachedResult != null) {
            log.info("从缓存返回航空路线数据，relationId: {}", relationId);
            return cachedResult;
        }
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            GlobalLocationRelation relation = validateRelation(relationId, "AIR");

            GlobalLocation origin = locationRepository.findById(relation.getSourceId())
                    .orElseThrow(() -> new EntityNotFoundException("源地点未找到"));

            GlobalLocation target = locationRepository.findById(relation.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("目标地点未找到"));
            // 获取最近机场
            AirportInfo originAirport = getClosestAirport(origin.getLatitude(), origin.getLongitude());
            AirportInfo destAirport = getClosestAirport(target.getLatitude(), target.getLongitude());
            // 获取三段路线
            Map<String, Object> landRouteToAirport = getLandRoute(
                    origin.getLatitude(), origin.getLongitude(),
                    originAirport.getLatitude(), originAirport.getLongitude());
            Map<String, Object> airRoute = getAirRoute(originAirport.getIata(), destAirport.getIata());
            Map<String, Object> landRouteFromAirport = getLandRoute(
                    destAirport.getLatitude(), destAirport.getLongitude(),
                    target.getLatitude(), target.getLongitude());
            // 计算总指标
            double totalDistance = 0;
            double totalDuration = 0;
            totalDistance += extractDistance(landRouteToAirport);
            totalDuration += extractDuration(landRouteToAirport);
            totalDistance += extractDistance(airRoute);
            totalDuration += extractDuration(airRoute);
            totalDistance += extractDistance(landRouteFromAirport);
            totalDuration += extractDuration(landRouteFromAirport);

            // 构建响应
            result.put("relationId", relationId);
            result.put("transportMode", "AIR");
            result.put("totalDistanceKm", totalDistance / 1000); // 转为公里
            result.put("totalDurationHours", totalDuration / 3600); // 转为小时
            result.put("origin", buildLocationInfo(origin));
            result.put("destination", buildLocationInfo(target));
            result.put("originAirport", buildAirportInfo(originAirport));
            result.put("destinationAirport", buildAirportInfo(destAirport));
            result.put("landRouteToOriginAirport", landRouteToAirport);
            result.put("airRoute", airRoute);
            result.put("landRouteFromDestAirport", landRouteFromAirport);

            // 保存缓存
            pathCacheService.savePathCache(
                    relationId,
                    "AIR",
                    origin.getId(),
                    target.getId(),
                    origin.getName(),
                    target.getName(),
                    result
            );

        } catch (Exception e) {
            log.error("航空路线处理失败", e);
            result.put("error", e.getMessage());
        }
        return result;
    }

    private Map<String, Object> buildAirportInfo(AirportInfo airport) {
        Map<String, Object> map = new HashMap<>();
        map.put("iata", airport.getIata());
        map.put("name", airport.getName());
        map.put("coordinates", Arrays.asList(airport.getLatitude(), airport.getLongitude()));
        return map;
    }




}