package shiyee_FYP.fullstack_backend.security;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class RestTemplateConfig {

    @Bean
    public String googleMapsApiKey() {
        return "your-google-maps-api-key"; // Replace with your actual key
    }


//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate(createClientHttpRequestFactory());
}

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        // 设置代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(proxy);
        return factory;
    }

}