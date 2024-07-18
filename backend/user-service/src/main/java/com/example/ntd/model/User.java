package com.example.ntd.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Data
public class User {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  @NotBlank
  private String password;

  @Convert(converter = UserStatusEnumConverter.class)
  @Column(nullable = false)
  private UserStatusEnum status;

  @Column(nullable = false)
  private BigDecimal balance;
}