package shiyee_FYP.fullstack_backend.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.PathCache;
import shiyee_FYP.fullstack_backend.repository.PathCacheRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PathCacheService {
    private final PathCacheRepository pathCacheRepository;
    private final ObjectMapper objectMapper;

    public void savePathCache(Long relationId, String transportMode,
                              Long sourceId, Long targetId,
                              String sourceName, String targetName,
                              Map<String, Object> pathData) {
        try {
            log.info("开始保存缓存，relationId: {}", relationId);

            // 1. 创建精简数据
            Map<String, Object> simplifiedData = createSimplifiedData(transportMode, pathData);

            // 2. 构建缓存实体
            PathCache cache = buildCacheEntity(
                    relationId, transportMode,
                    sourceId, targetId,
                    sourceName, targetName,
                    pathData, simplifiedData
            );

            // 3. 保存到数据库
            pathCacheRepository.save(cache);
            log.info("缓存保存成功，relationId: {}", relationId);

        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败 - relationId: {}", relationId, e);
        } catch (ClassCastException e) {
            log.error("数据类型转换失败 - relationId: {}", relationId, e);
        } catch (Exception e) {
            log.error("未知缓存保存错误 - relationId: {}", relationId, e);
        }
    }

    // 辅助方法：创建精简数据
    private Map<String, Object> createSimplifiedData(String transportMode,
                                                     Map<String, Object> pathData) {
        Map<String, Object> simplified = new LinkedHashMap<>();

        // 必填基础字段
        simplified.put("totalDistanceKm", pathData.get("totalDistanceKm"));
        simplified.put("totalDurationHours", pathData.get("totalDurationHours"));
        simplified.put("transportMode", transportMode);

        // 海运特有字段
        if ("SHIPPING".equals(transportMode)) {
            try {
                Map<?, ?> originPort = (Map<?, ?>) pathData.get("originPort");
                Map<?, ?> destPort = (Map<?, ?>) pathData.get("destinationPort");

                simplified.put("originPortName", originPort.get("name"));
                simplified.put("destPortName", destPort.get("name"));
                simplified.put("originPortLocode", originPort.get("locode"));
            } catch (Exception e) {
                log.warn("海运港口信息提取失败", e);
            }
        }if ("ROAD".equals(transportMode)) {
            try {
                Map<?, ?> origin = (Map<?, ?>) pathData.get("origin");
                Map<?, ?> destination = (Map<?, ?>) pathData.get("destination");

                simplified.put("originName", origin.get("name"));
                simplified.put("destName", destination.get("name"));
            } catch (Exception e) {
                log.warn("公路路线信息提取失败", e);
            }
        }


        return simplified;
    }

    // 辅助方法：构建缓存实体
    private PathCache buildCacheEntity(Long relationId, String transportMode,
                                       Long sourceId, Long targetId,
                                       String sourceName, String targetName,
                                       Map<String, Object> pathData,
                                       Map<String, Object> simplifiedData)
            throws JsonProcessingException {

        PathCache cache = new PathCache();
        cache.setRelationId(relationId);
        cache.setTransportMode(transportMode);
        cache.setSourceId(sourceId);
        cache.setTargetId(targetId);
        cache.setSourceName(sourceName);
        cache.setTargetName(targetName);

        // 设置计算指标
        if (pathData.get("totalDistanceKm") instanceof Number) {
            cache.setTotalDistance(((Number) pathData.get("totalDistanceKm")).doubleValue());
        }
        if (pathData.get("totalDurationHours") instanceof Number) {
            cache.setTotalDuration(((Number) pathData.get("totalDurationHours")).doubleValue());
        }

        // 序列化精简数据
//        cache.setPathData(objectMapper.writeValueAsString(simplifiedData));
        cache.setPathData(objectMapper.writeValueAsString(pathData));

        return cache;
    }

    public Map<String, Object> getPathCache(Long relationId) {
        PathCache cache = pathCacheRepository.findByRelationId(relationId);
        if (cache != null) {
            try {
                Map<String, Object> result = new HashMap<>();
                result.put("relationId", cache.getRelationId());
                result.put("transportMode", cache.getTransportMode());
                result.put("totalDistanceKm", cache.getTotalDistance());
                result.put("totalDurationHours", cache.getTotalDuration());

                // 合并原始路径数据（如果有）
                if (cache.getPathData() != null) {
                    Map<String, Object> pathData = objectMapper.readValue(
                            cache.getPathData(),
                            new TypeReference<Map<String, Object>>() {}
                    );
                    result.putAll(pathData);
                }

                result.put("fromCache", true);
                return result;
            } catch (Exception e) {
                log.error("缓存数据解析失败", e);
            }
        }
        return null;
    }
}