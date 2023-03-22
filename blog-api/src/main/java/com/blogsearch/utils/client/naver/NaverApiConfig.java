package com.blogsearch.utils.client.naver;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NaverApiConfig {

    @Value("${search.source.naver.client-id}")
    private String clientId;

    @Value("${search.source.naver.client-secret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate.header("X-Naver-Client-Id", clientId)
                        .header("X-Naver-Client-Secret", clientSecret);
    }

}
