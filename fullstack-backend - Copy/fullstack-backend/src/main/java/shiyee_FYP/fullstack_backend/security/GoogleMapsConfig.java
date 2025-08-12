package shiyee_FYP.fullstack_backend.security;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.Service.GoogleMapsService;

@Configuration
public class GoogleMapsConfig {

    @Value("${global.google.maps.api.key}")
    private String apiKey;

    @Bean
    public RestTemplate googleMapsRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GoogleMapsService googleMapsService(RestTemplate restTemplate) {
        return new GoogleMapsService(restTemplate, apiKey);
    }

}
