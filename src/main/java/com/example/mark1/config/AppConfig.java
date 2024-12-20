package com.example.mark1.config;


import com.example.mark1.utils.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }
}
