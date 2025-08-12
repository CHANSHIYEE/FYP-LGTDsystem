package shiyee_FYP.fullstack_backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import shiyee_FYP.fullstack_backend.model.DTO.LocationTypeStats;
import shiyee_FYP.fullstack_backend.model.DTO.ResearchCenterGeo;
import shiyee_FYP.fullstack_backend.model.DTO.TechRedundancyAssessment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LocationRedundancyService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LocationTypeStats> getAllTypeRedundancy() {
        // 1. 获取所有类型（更可靠的ENUM值获取方式）
        List<String> allTypes = Arrays.asList(
                "RESEARCH", "WAREHOUSE", "FACTORY", "SUPPLIER",
                "PROTOTYPE", "DISTRIBUTION", "MANUFACTURER", "TESTING"
        );
        // 2. 为每个类型计算指标
        return allTypes.stream().map(type -> {
            LocationTypeStats stats = new LocationTypeStats();
            stats.setType(type);
            try {
                Map<String, Object> base = jdbcTemplate.queryForMap(
                        "SELECT " +
                                "   COUNT(*) AS total, " +
                                "   COALESCE(SUM(CASE WHEN r.id IS NOT NULL THEN 1 ELSE 0 END), 0) AS backed_up, " +
                                "   COALESCE(ROUND(STDDEV(latitude) + STDDEV(longitude), 2), 0) AS geo_dispersion " +
                                "FROM locations l " +
                                "LEFT JOIN location_relations r ON l.id = r.target_id AND r.relation_type LIKE '%BACKUP%' " +
                                "WHERE l.type = ?",
                        type);

                stats.setTotalCount(convertToInt(base.get("total")));
                stats.setBackedUpCount(convertToInt(base.get("backed_up")));
                stats.setBackupRate(stats.getTotalCount() > 0 ?
                        (stats.getBackedUpCount() * 100.0) / stats.getTotalCount() : 0);
                stats.setGeoDispersion(convertToDouble(base.get("geo_dispersion")));
            } catch (EmptyResultDataAccessException e) {
                stats.setTotalCount(0);
                stats.setBackedUpCount(0);
                stats.setBackupRate(0);
                stats.setGeoDispersion(0);
            }
            return stats;
        }).collect(Collectors.toList());
    }

    // 安全转换方法
    private int convertToInt(Object num) {
        return num == null ? 0 : ((Number)num).intValue();
    }

    private double convertToDouble(Object num) {
        return num == null ? 0 : ((Number)num).doubleValue();
    }

    // 获取特定类型的详细设施
    public List<Map<String, Object>> getLocationsByType(String type) {
        return jdbcTemplate.queryForList(
                "SELECT l.*, " +
                        "   (SELECT COUNT(*) FROM location_relations r " +
                        "    WHERE r.target_id = l.id AND r.relation_type LIKE '%BACKUP%') AS backup_count " +
                        "FROM locations l " +
                        "WHERE l.type = ?",
                type);
    }

    public Double getRedundancyScore(Integer locationId) {
        try {
            // 计算备份比例
            Integer backupCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM location_relations " +
                            "WHERE target_id = ? AND relation_type LIKE '%BACKUP%'",
                    Integer.class, locationId);

            // 计算地理分散度 (0-1标准化)
            Double geoDispersion = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(STDDEV(latitude) + STDDEV(longitude), 0) " +
                            "FROM locations WHERE id IN (" +
                            "   SELECT source_id FROM location_relations " +
                            "   WHERE target_id = ? AND relation_type LIKE '%BACKUP%'" +
                            ")",
                    Double.class, locationId);

            // 标准化地理分散度 (假设最大可能值为10)
            double normalizedGeoDispersion = Math.min(geoDispersion / 10.0, 1.0);

            // 综合评分 (备份比例60% + 地理分散度40%)
            double score = 0.6 * (backupCount > 0 ? 1 : 0) + 0.4 * normalizedGeoDispersion;

            return score;
        } catch (EmptyResultDataAccessException e) {
            return null; // 无数据时返回null
        }
    }


}