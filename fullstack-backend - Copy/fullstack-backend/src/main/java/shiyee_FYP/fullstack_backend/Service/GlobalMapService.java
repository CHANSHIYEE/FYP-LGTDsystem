package shiyee_FYP.fullstack_backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GlobalMapService {

    @Value("${global.google.maps.api.key}")
    private String apiKey;

    @Value("${global.google.maps.geocode.url}")
    private String geocodeUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public GlobalMapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}


