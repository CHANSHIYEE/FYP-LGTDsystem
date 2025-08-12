package shiyee_FYP.fullstack_backend.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.DTO.Route;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GoogleMapsService {
    private final RestTemplate restTemplate;
    private final String apiKey;

    private static final String DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";

    public GoogleMapsService(RestTemplate restTemplate, String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public List<String> getDirections(double originLat, double originLng,
                                      double destLat, double destLng) {
        String url = String.format("%s?origin=%f,%f&destination=%f,%f&key=%s",
                DIRECTIONS_API_URL, originLat, originLng, destLat, destLng, apiKey);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return parseDirections(response.getBody());
        }
        return Collections.emptyList();
    }

    private List<String> parseDirections(Map<String, Object> response) {
        List<String> directions = new ArrayList<>();
        List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");

        if (routes != null && !routes.isEmpty()) {
            List<Map<String, Object>> legs = (List<Map<String, Object>>) routes.get(0).get("legs");
            for (Map<String, Object> leg : legs) {
                List<Map<String, Object>> steps = (List<Map<String, Object>>) leg.get("steps");
                for (Map<String, Object> step : steps) {
                    directions.add((String) step.get("html_instructions"));
                }
            }
        }
        return directions;
    }


}


//@Service
//public class GoogleMapsService {
//
//    private final String apiKey;
//    private final RestTemplate restTemplate;
//
//    @Autowired // 明确添加注解（可选但更清晰）
//    public GoogleMapsService(
//            @Value("${google.maps.api.key}") String apiKey,
//            RestTemplate restTemplate) {
//        this.apiKey = apiKey;
//        this.restTemplate = restTemplate;
//
//        // 验证API Key是否配置
//        if (apiKey == null || apiKey.isEmpty()) {
//            throw new IllegalStateException("Google Maps API key is not configured");
//        }
//    }
//
//    // 1. Directions API获取路径
//    public String getDirections(double originLat, double originLng,
//                                double destLat, double destLng,
//                                String mode) {
//        String url = String.format(
//                "https://maps.googleapis.com/maps/api/directions/json" +
//                        "?origin=%s,%s&destination=%s,%s&mode=%s&key=%s",
//                originLat, originLng, destLat, destLng, mode, apiKey
//        );
//
//        return restTemplate.getForObject(url, String.class);
//    }
//
//    // 2. Distance Matrix API获取距离和时长
//    public String getDistanceMatrix(double originLat, double originLng,
//                                    double destLat, double destLng) {
//        String url = String.format(
//                "https://maps.googleapis.com/maps/api/distancematrix/json" +
//                        "?origins=%s,%s&destinations=%s,%s&key=%s",
//                originLat, originLng, destLat, destLng, apiKey
//        );
//
//        return restTemplate.getForObject(url, String.class);
//    }
//}