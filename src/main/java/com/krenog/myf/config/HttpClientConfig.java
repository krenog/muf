package com.krenog.myf.config;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class HttpClientConfig {
    @Value("${httpclient.pool.max-size}")
    private Integer maxPoolSize;
    @Value("${httpclient.pool.per-route-size}")
    private Integer perRouteSize;
    @Value("${httpclient.timeout.connection}")
    private Integer connectionTimeout;
    @Value("${httpclient.timeout.socket}")
    private Integer socketTimeout;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(clientFactory(null));
        return template;
    }

    private HttpComponentsClientHttpRequestFactory clientFactory(SSLContext sslContext) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxPoolSize);
        cm.setDefaultMaxPerRoute(perRouteSize);
        RequestConfig.Builder requestBuilder = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout);
        HttpClientBuilder builder = HttpClientBuilder.create()
                .setConnectionManager(cm)
                .disableCookieManagement()
                .setDefaultRequestConfig(requestBuilder.build())
                .addInterceptorFirst((HttpRequestInterceptor) new HttpClientLoggingInterceptor())
                .addInterceptorLast((HttpResponseInterceptor) new HttpClientLoggingInterceptor());
        if (sslContext != null) {
            builder.setSSLContext(sslContext);
        }
        return new HttpComponentsClientHttpRequestFactory(builder.build());
    }
}
