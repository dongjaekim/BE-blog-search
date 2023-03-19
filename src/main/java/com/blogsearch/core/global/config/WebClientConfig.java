package com.blogsearch.core.global.config;

import com.blogsearch.core.service.external.ExternalSearchService;
import com.blogsearch.core.service.external.KakaoSearchService;
import com.blogsearch.core.service.external.NaverSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public ReactorResourceFactory reactorResourceFactory(){
        return new ReactorResourceFactory();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public ExternalSearchService externalSearchService() {
//        return new KakaoSearchService(webClient());
        return new NaverSearchService(webClient());
    }
}
