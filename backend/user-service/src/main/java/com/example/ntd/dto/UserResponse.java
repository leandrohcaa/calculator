package com.example.ntd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UserResponse {

  private UUID id;

  private Boolean isActive;

  private BigDecimal balance;
}