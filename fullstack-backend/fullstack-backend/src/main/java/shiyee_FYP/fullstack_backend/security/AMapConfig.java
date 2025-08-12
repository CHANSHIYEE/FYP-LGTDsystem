package shiyee_FYP.fullstack_backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class AMapConfig {

    @Bean("aMapRestTemplate")
    public RestTemplate aMapRestTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(15)) // 高德API需要更长的超时
                .setReadTimeout(Duration.ofSeconds(45))
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "amap")
    public AMapProperties aMapProperties() {
        return new AMapProperties();
    }

    @Data
    public static class AMapProperties {
        private String key;
        private String baseUrl = "https://restapi.amap.com/v3";
    }
}