package com.example.ntd.service;

import com.example.ntd.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.web.reactive.function.client.WebClient;
        import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserService {

  @Autowired
  private WebClient userWebClient;

  public Mono<UserResponse> getUserByUsername(String username, String token) {
    return userWebClient.get()
            .uri(uriBuilder -> uriBuilder.path("/api/v1/users/{username}")
                    .build(username))
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(UserResponse.class);
  }

  public Mono<UserResponse> updateUserBalance(UUID id, BigDecimal balance, String token) {
    return userWebClient.put()
            .uri(uriBuilder -> uriBuilder.path("/api/v1/users/{id}/balance").build(id))
            .header("Authorization", token)
            .bodyValue(balance)
            .retrieve()
            .bodyToMono(UserResponse.class);
  }
}