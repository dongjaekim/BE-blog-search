package com.blogsearch.global.config;

import com.blogsearch.global.aop.SocketAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SocketAspect socketAspect() {
        return new SocketAspect();
    }
}
