package com.example.ntd.dto;

import com.example.ntd.model.Operation;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RecordResponse {

  private UUID id;

  private String operationId;

  private UUID userId;

  private BigDecimal amount;

  private BigDecimal userBalance;

  private String operationResponse;

  private LocalDateTime date;

  private Boolean deleted;
}