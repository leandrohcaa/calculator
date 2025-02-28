package com.example.ntd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Value("${urls.user-service}")
  private String userBaseUrl;

  @Bean
  public WebClient userWebClient() {
    return WebClient.builder()
            .baseUrl(userBaseUrl)
            .build();
  }
}