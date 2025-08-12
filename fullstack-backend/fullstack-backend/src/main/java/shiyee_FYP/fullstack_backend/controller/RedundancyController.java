package shiyee_FYP.fullstack_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import shiyee_FYP.fullstack_backend.Service.LocationRedundancyService;
import shiyee_FYP.fullstack_backend.model.DTO.LocationTypeStats;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/R")
public class RedundancyController {

    @Autowired
    private LocationRedundancyService redundancyService;

    // 获取全类型冗余度报告
    @GetMapping("/redundancy-report")
    public ResponseEntity<?> getRedundancyReport() {
        try {
            return ResponseEntity.ok(redundancyService.getAllTypeRedundancy());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Failed to generate report",
                            "details", e.getMessage()
                    ));
        }
    }

    // 按类型查询设施详情
    @GetMapping("/by-type/{type}")
    public List<Map<String, Object>> getByType(
            @PathVariable String type,
            @RequestParam(required = false) Boolean hasBackup) {

        List<Map<String, Object>> result = redundancyService.getLocationsByType(type);

        // 可选过滤
        if (hasBackup != null) {
            result = result.stream()
                    .filter(loc -> ((Number)loc.get("backup_count")).intValue() > 0 == hasBackup)
                    .collect(Collectors.toList());
        }

        return result;
    }
}