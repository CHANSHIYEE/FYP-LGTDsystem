package shiyee_FYP.fullstack_backend.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.AmapRouteResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class AmapService {
    @Value("${amap.api.key}")
    private String apiKey;

    @Value("${amap.route.url}")
    private String routeUrl;

    private final RestTemplate restTemplate;

    public AmapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AmapRouteResponse getDrivingRoute(String origin, String destination) {
        Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("origin", origin);
        params.put("destination", destination);
        params.put("strategy", "0"); // 0=速度优先

        String url = routeUrl + "?key={key}&origin={origin}&destination={destination}&strategy={strategy}";

        return restTemplate.getForObject(url, AmapRouteResponse.class, params);
    }
}
