package com.example.ntd.dto;

import lombok.Data;

@Data
public class PerformOperationResponse {

  private String result;

  public PerformOperationResponse(String result) {
    this.result = result;
  }
}