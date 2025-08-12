package shiyee_FYP.fullstack_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GlobalConfig {
    @Bean
    public RestTemplate globalRestTemplate() {
        return new RestTemplate();
    }
}