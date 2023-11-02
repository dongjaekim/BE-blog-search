package com.blogsearch.global.config;

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

}
