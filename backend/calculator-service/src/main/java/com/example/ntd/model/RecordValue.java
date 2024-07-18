package com.example.ntd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Data
public class RecordValue {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "operation_id", nullable = false)
  private Operation operation;

  @Column(nullable = false)
  private UUID userId;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  @NotNull
  private BigDecimal userBalance;

  @Column(nullable = false)
  private String operationResponse;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false)
  private Boolean deleted = false;
}