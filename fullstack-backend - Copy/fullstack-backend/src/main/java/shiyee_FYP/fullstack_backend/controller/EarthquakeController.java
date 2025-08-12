package shiyee_FYP.fullstack_backend.controller;

import shiyee_FYP.fullstack_backend.Service.EarthquakeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/earthquakes")
@CrossOrigin // 如果React和Spring Boot是分开部署的，跨域支持
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }

    /**
     * 获取地震列表
     * @param period 查询时间范围，支持 hour/day/week
     * @param minMagnitude 最小震级筛选（默认0）
     * @return List<Map<String, Object>>
     */
    @GetMapping
    public List<Map<String, Object>> getEarthquakes(
            @RequestParam(defaultValue = "hour") String period,
            @RequestParam(defaultValue = "0") double minMagnitude) {
        return earthquakeService.fetchEarthquakes(period, minMagnitude);
    }
}
