
package shiyee_FYP.fullstack_backend.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.LocRelation;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.repository.LocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.LocationRepository;
import shiyee_FYP.fullstack_backend.model.ApiResponse;
import shiyee_FYP.fullstack_backend.model.AmapRouteResponse;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationRepository locationRepository;
    private final LocationRelationRepository relationRepository;
    private final RestTemplate restTemplate;

    @Value("${amap.api.key}")
    private String apiKey;

    @Value("${amap.route.url}")
    private String routeUrl;

    // 1. 获取所有地点坐标
    @GetMapping("/coordinates")
    public ResponseEntity<List<Location>> getAllLocations() {
        try {
            List<Location> locations = locationRepository.findAll();
            log.info("获取到 {} 条地点数据", locations.size());
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            log.error("获取地点数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/types")
    public ResponseEntity<List<Map<String, Object>>> getLocationTypesWithIds() {
        try {
            List<Map<String, Object>> locationTypes = locationRepository.findAll()
                    .stream()
                    .map(location -> {
                        Map<String, Object> locationInfo = new HashMap<>();
                        locationInfo.put("id", location.getId());
                        locationInfo.put("type", location.getType());
                        locationInfo.put("name", location.getName());
                        locationInfo.put("longitude", location.getLongitude());
                        locationInfo.put("latitude", location.getLatitude());
                        locationInfo.put("vulnerabilityScore", location.getVulnerabilityScore());
                        return locationInfo;
                    })
                    .collect(Collectors.toList());

            log.info("获取到 {} 条地点类型数据", locationTypes.size());
            return ResponseEntity.ok(locationTypes);
        } catch (Exception e) {
            log.error("获取地点类型数据失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. 获取所有地点关系
    @GetMapping("/relations")
    public ResponseEntity<?> getAllRelations() {
        try {
            List<LocRelation> relations = relationRepository.findAll();
            List<Map<String, Object>> result = relations.stream().map(rel -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", rel.getId());
                map.put("sourceId", rel.getSource().getId());
                map.put("targetId", rel.getTarget().getId());
                map.put("relationType", rel.getRelationType().name());
                map.put("createdAt", rel.getCreatedAt());
                return map;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("获取关系数据失败", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "获取数据失败", "reason", e.getMessage()));
        }
    }

    // 3. 计算最短路径（基于实际道路）
    @GetMapping("/shortest-path/{startId}/{endId}")
    public ResponseEntity<ApiResponse<AmapRouteResponse>> calculateShortestPath(
            @PathVariable Integer startId,
            @PathVariable Integer endId) {

        try {
            // 验证起点终点
            Location start = locationRepository.findById(startId)
                    .orElseThrow(() -> new RuntimeException("起点不存在"));
            Location end = locationRepository.findById(endId)
                    .orElseThrow(() -> new RuntimeException("终点不存在"));

            // 构建高德API参数
            String origin = String.format("%f,%f", start.getLongitude(), start.getLatitude());
            String destination = String.format("%f,%f", end.getLongitude(), end.getLatitude());

            // 调用高德API（策略2=最短距离，extensions=base获取详细路径）
            String url = String.format("%s?key=%s&origin=%s&destination=%s&strategy=2&extensions=base",
                    routeUrl, apiKey, origin, destination);

            AmapRouteResponse response = restTemplate.getForObject(url, AmapRouteResponse.class);

            // 验证响应
            if (response == null || response.getStatus() != 1) {
                throw new RuntimeException(response != null ? response.getInfo() : "API无响应");
            }

            // 检查是否为直线路径
            if (isStraightLinePath(response)) {
                throw new RuntimeException("高德返回了直线路径，请检查坐标点是否有效");
            }

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("路径计算失败: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 4. 计算特定关系的路径
    @PostMapping("/relations/{relationId}/calculate")
    public ResponseEntity<ApiResponse<AmapRouteResponse>> calculateRelationRoute(
            @PathVariable Integer relationId) {

        try {
            LocRelation relation = relationRepository.findById(relationId)
                    .orElseThrow(() -> new RuntimeException("关系不存在"));

            // 构建请求参数
            String origin = String.format("%f,%f",
                    relation.getSource().getLongitude(),
                    relation.getSource().getLatitude());
            String destination = String.format("%f,%f",
                    relation.getTarget().getLongitude(),
                    relation.getTarget().getLatitude());

            // 调用高德API
            String url = String.format("%s?key=%s&origin=%s&destination=%s&strategy=2&extensions=base",
                    routeUrl, apiKey, origin, destination);

            AmapRouteResponse response = restTemplate.getForObject(url, AmapRouteResponse.class);

            if (response == null || response.getStatus() != 1) {
                throw new RuntimeException(response != null ? response.getInfo() : "API无响应");
            }

            if (isStraightLinePath(response)) {
                throw new RuntimeException("获取到直线路径，请检查坐标点");
            }

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 5. 通用路径计算接口
    @PostMapping("/shortpath")
    public ResponseEntity<ApiResponse<AmapRouteResponse>> calculateRoute(
            @RequestBody RouteCalculationRequest request) {

            log.info("收到路径计算请求: {}", request); // 添加这行

        try {
            // 参数校验
            if (request.getOrigin() == null || request.getDestination() == null) {
                throw new RuntimeException("必须提供起点和终点坐标");
            }

            // 调用高德API
            String url = String.format("%s?key=%s&origin=%s&destination=%s&strategy=2&extensions=base",
                    routeUrl, apiKey, request.getOrigin(), request.getDestination());

            AmapRouteResponse response = restTemplate.getForObject(url, AmapRouteResponse.class);

            if (response == null || response.getStatus() != 1) {
                throw new RuntimeException(response != null ? response.getInfo() : "API无响应");
            }

            if (isStraightLinePath(response)) {
                throw new RuntimeException("路径无效：返回了直线距离");
            }

            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // 判断是否为直线路径
    private boolean isStraightLinePath(AmapRouteResponse response) {
        if (response.getRoute() == null ||
                response.getRoute().getPaths() == null ||
                response.getRoute().getPaths().isEmpty()) {
            return true;
        }

        AmapRouteResponse.Path path = response.getRoute().getPaths().get(0);

        // 条件1：没有步骤或只有1个步骤
        if (path.getSteps() == null || path.getSteps().size() <= 1) {
            return true;
        }

        // 条件2：所有步骤的折线点都少于2个
        boolean allStepsSimple = path.getSteps().stream()
                .allMatch(step -> step.getPolyline() == null ||
                        step.getPolyline().split(";").length < 2);

        return allStepsSimple;
    }

    // 内部请求参数类
    @Data
    static class RouteCalculationRequest {
        private String origin;      // 格式: "经度,纬度" (如 "116.481028,39.989643")
        private String destination; // 格式: "经度,纬度"
    }
}