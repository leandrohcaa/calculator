package com.example.ntd.controller;

import com.example.ntd.dto.UserResponse;
import com.example.ntd.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API for managing users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{username}")
  @Operation(summary = "Get user by username", description = "Get a user by username")
  public ResponseEntity<UserResponse> getUserById(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUserByUsername(username));
  }

  @PutMapping("/{id}/balance")
  @Operation(summary = "Set the user balance", description = "Set the user balance")
  public ResponseEntity<UserResponse> updateUserBalance(
          @PathVariable UUID id, @RequestBody BigDecimal balance) {
    return ResponseEntity.ok(userService.updateBalance(id, balance));
  }
}