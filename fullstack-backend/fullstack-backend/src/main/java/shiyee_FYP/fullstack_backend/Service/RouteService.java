package shiyee_FYP.fullstack_backend.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.AmapRouteResponse;
import shiyee_FYP.fullstack_backend.model.Coordinate;
import shiyee_FYP.fullstack_backend.repository.CoordinateRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {
    private final CoordinateRepository coordinateRepository;
    private final RestTemplate restTemplate;

    @Value("${amap.api.key}")
    private String apiKey;

    @Value("${amap.route.url}")
    private String routeUrl;

    // 获取用户坐标点列表
    public List<Coordinate> getUserCoordinates(Long userId) {
        return coordinateRepository.findByUserId(userId);
    }

    // 调用高德API计算路径
    public AmapRouteResponse calculateRoute(Double startLat, Double startLng,
                                            Double endLat, Double endLng) {
        try {
            String origin = startLng + "," + startLat;
            String destination = endLng + "," + endLat;

            String url = String.format("%s?key=%s&origin=%s&destination=%s&strategy=2",
                    routeUrl, apiKey, origin, destination);

            log.info("Calling AMap API with URL: {}", url.replace(apiKey, "***"));

            AmapRouteResponse response = restTemplate.getForObject(url, AmapRouteResponse.class);

            if (response == null || response.getStatus() != 1) {
                throw new RuntimeException("高德API调用失败: " +
                        (response != null ? response.getInfo() : "无响应"));
            }

            return response;
        } catch (Exception e) {
            log.error("路径计算失败: {}", e.getMessage());
            throw new RuntimeException("路径计算失败: " + e.getMessage());
        }
    }
}
