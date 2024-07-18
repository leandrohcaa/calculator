package com.example.ntd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PerformOperationRequest {

  private String username;
  private UUID operationId;
  private BigDecimal amount;

}