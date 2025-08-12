package shiyee_FYP.fullstack_backend.security;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.net.URI;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

@Configuration
public class RestTemplateConfig {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(createClientHttpRequestFactory());

        // 添加拦截器：为每个请求自动添加 API Key 参数
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution) throws IOException {
                URI originalUri = request.getURI();

                try {
                    // 构造新的 URI 并追加 API key 参数
                    String query = originalUri.getQuery();
                    String newQuery = (query == null ? "" : query + "&") + "key=" + googleMapsApiKey;

                    URI newUri = new URI(
                            originalUri.getScheme(),
                            originalUri.getAuthority(),
                            originalUri.getPath(),
                            newQuery,
                            originalUri.getFragment()
                    );

                    HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                        @Override
                        public URI getURI() {
                            return newUri;
                        }
                    };

                    return execution.execute(modifiedRequest, body);
                } catch (URISyntaxException e) {
                    throw new IOException("Invalid URI syntax: " + e.getMessage(), e);
                }
            }
        });

        return restTemplate;
    }

    private ClientHttpRequestFactory createClientHttpRequestFactory() {
        // 如果你需要通过代理访问 API，这里可以配置
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setProxy(proxy);
        return factory;
    }
}


//
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    public String googleMapsApiKey() {
//        return "your-google-maps-api-key"; // Replace with your actual key
//    }
//
//@Bean
//public RestTemplate restTemplate() {
//    return new RestTemplate(createClientHttpRequestFactory());
//}
//
//    private ClientHttpRequestFactory createClientHttpRequestFactory() {
//        // 设置代理
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 33210));
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setProxy(proxy);
//        return factory;
//    }
//
//}