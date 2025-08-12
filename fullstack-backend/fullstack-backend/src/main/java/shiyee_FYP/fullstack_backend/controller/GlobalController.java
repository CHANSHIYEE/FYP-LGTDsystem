package shiyee_FYP.fullstack_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import shiyee_FYP.fullstack_backend.Service.GlobalService;
import shiyee_FYP.fullstack_backend.Service.PathCacheService;
import shiyee_FYP.fullstack_backend.Service.ShippingRouteService;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;
import shiyee_FYP.fullstack_backend.model.Location;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;
@Slf4j
@RestController
@RequestMapping("/api/global")
public class GlobalController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GlobalService globalService;
    @Autowired
    private ShippingRouteService shippingRouteService;
    @Autowired
    private GlobalLocationRepository globalLocationRepository;

    @Autowired
    private GlobalLocationRelationRepository globalLocationRelationRepository;
    @GetMapping("/coordinates")
    public ResponseEntity<List<Map<String, Object>>> getAllLocationsWithCoordinates() {
        try {
            List<GlobalLocation> locations = globalService.getAllLocations();
            List<Map<String, Object>> response = new ArrayList<>();

            for (GlobalLocation location : locations) {
                Map<String, Object> locationData = new HashMap<>();
                locationData.put("id", location.getId());
                locationData.put("name", location.getName());
                locationData.put("longitude", location.getLongitude());
                locationData.put("latitude", location.getLatitude());
                response.add(locationData);
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching locations with coordinates", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(
                            Collections.singletonMap("error", "Failed to fetch locations with coordinates: " + e.getMessage())
                    ));
        }
    }

    @GetMapping("/relations")
    public ResponseEntity<List<GlobalLocationRelation>> getAllRelations() {
        List<GlobalLocationRelation> relations = globalService.getAllRelations();
        return ResponseEntity.ok(relations);
    }

    @GetMapping("/relation/{id}")
    public ResponseEntity<GlobalLocationRelation> getRelationById(@PathVariable Long id) {

        Optional<GlobalLocationRelation> relationOptional = globalLocationRelationRepository.findById(id);
        if (relationOptional.isPresent()) {

            return ResponseEntity.ok(relationOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Autowired
    private PathCacheService pathCacheService;


    @GetMapping("/direction/{relationId}")
    public ResponseEntity<?> getUnifiedDirection(@PathVariable Long relationId) {
        try {
            // 先查缓存
            Map<String, Object> cachedResult = pathCacheService.getPathCache(relationId);
            if (cachedResult != null) {
                log.info("从缓存返回路线数据，relationId: {}", relationId);
                return ResponseEntity.ok(cachedResult);
            }

            GlobalLocationRelation relation = globalLocationRelationRepository.findById(relationId)
                    .orElseThrow(() -> new EntityNotFoundException("关系未找到"));

            if ("ROAD".equalsIgnoreCase(relation.getTransportMode())) {
                String result = globalService.getDirectionByRelationId(relationId);
                Map<String, Object> resultMap = objectMapper.readValue(result, HashMap.class);

                GlobalLocation origin = globalLocationRepository.findById(relation.getSourceId())
                        .orElseThrow(() -> new EntityNotFoundException("源地点未找到"));
                GlobalLocation target = globalLocationRepository.findById(relation.getTargetId())
                        .orElseThrow(() -> new EntityNotFoundException("目标地点未找到"));

                double distance = shippingRouteService.extractDistance(resultMap); // 米
                double duration = shippingRouteService.extractDuration(resultMap); // 秒

                resultMap.put("totalDistanceKm", distance / 1000); // 转换成公里
                resultMap.put("totalDurationHours", duration / 3600); // 转换成小时


                pathCacheService.savePathCache(
                        relationId,
                        "ROAD",
                        origin.getId(),
                        target.getId(),
                        origin.getName(),
                        target.getName(),
                        resultMap
                );

                return ResponseEntity.ok(resultMap);
            }
            else if ("SHIPPING".equalsIgnoreCase(relation.getTransportMode())) {
                Map<String, Object> result = shippingRouteService.getFullShippingRoute(relationId);
                if (result.containsKey("error")) {
                    return ResponseEntity.badRequest().body(result);
                }
                return ResponseEntity.ok(result);
            }
            else if ("AIR".equalsIgnoreCase(relation.getTransportMode())) {
                Map<String, Object> result = shippingRouteService.getFullAirRoute(relationId);
                if (result.containsKey("error")) {
                    return ResponseEntity.badRequest().body(result);
                }
                return ResponseEntity.ok(result);
            }
            else {
                return ResponseEntity.badRequest().body(
                        Collections.singletonMap("error", "不支持的运输模式: " + relation.getTransportMode())
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Collections.singletonMap("error", "方向请求处理错误: " + e.getMessage())
            );
        }
    }

//    @GetMapping("/direction/{relationId}")
//    public ResponseEntity<?> getUnifiedDirection(@PathVariable Long relationId) {
//        try {
//            // ✅ 先查缓存（对所有模式都适用）
//            Map<String, Object> cachedResult = pathCacheService.getPathCache(relationId);
//            if (cachedResult != null) {
//                log.info("从缓存返回路线数据，relationId: {}", relationId);
//                return ResponseEntity.ok(cachedResult);
//            }
//
//            GlobalLocationRelation relation = globalLocationRelationRepository.findById(relationId)
//                    .orElseThrow(() -> new EntityNotFoundException("关系未找到"));
//
//            if ("ROAD".equalsIgnoreCase(relation.getTransportMode())) {
//                String result = globalService.getDirectionByRelationId(relationId);
//                Map<String, Object> resultMap = objectMapper.readValue(result, HashMap.class);
//
//                GlobalLocation origin = globalLocationRepository.findById(relation.getSourceId())
//                        .orElseThrow(() -> new EntityNotFoundException("源地点未找到"));
//                GlobalLocation target = globalLocationRepository.findById(relation.getTargetId())
//                        .orElseThrow(() -> new EntityNotFoundException("目标地点未找到"));
//
//                double distance = shippingRouteService.extractDistance(resultMap); // 米
//                double duration = shippingRouteService.extractDuration(resultMap); // 秒
//
//                resultMap.put("totalDistanceKm", distance / 1000); // 转换成公里
//                resultMap.put("totalDurationHours", duration / 3600); // 转换成小时
//
//
//                pathCacheService.savePathCache(
//                        relationId,
//                        "ROAD",
//                        origin.getId(),
//                        target.getId(),
//                        origin.getName(),
//                        target.getName(),
//                        resultMap
//                );
//
//                return ResponseEntity.ok(resultMap);
//            }
//
//
//            else if ("SHIPPING".equalsIgnoreCase(relation.getTransportMode())) {
//                Map<String, Object> result = shippingRouteService.getFullShippingRoute(relationId);
//                if (result.containsKey("error")) {
//                    return ResponseEntity.badRequest().body(result);
//                }
//                return ResponseEntity.ok(result);
//            }
//            else {
//                return ResponseEntity.badRequest().body(
//                        Collections.singletonMap("error", "不支持的运输模式: " + relation.getTransportMode())
//                );
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                    Collections.singletonMap("error", "方向请求处理错误: " + e.getMessage())
//            );
//        }
//    }

    // 添加新的缓存查询端点
    @GetMapping("/cache/{relationId}")
    public ResponseEntity<?> getCachedDirection(@PathVariable Long relationId) {
        Map<String, Object> cachedResult = pathCacheService.getPathCache(relationId);
        if (cachedResult != null) {
            return ResponseEntity.ok(cachedResult);
        }
        return ResponseEntity.notFound().build();
    }
}



