package com.example.ntd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponse {

  private UUID id;

  private Boolean isActive;

  private BigDecimal balance;
}