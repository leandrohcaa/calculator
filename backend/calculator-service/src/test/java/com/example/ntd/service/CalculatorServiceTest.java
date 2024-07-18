package com.example.ntd.service;

import com.example.ntd.dto.PerformOperationRequest;
import com.example.ntd.dto.PerformOperationResponse;
import com.example.ntd.dto.UserResponse;
import com.example.ntd.exception.InsufficientBalanceException;
import com.example.ntd.exception.OperationNotFoundException;
import com.example.ntd.model.Operation;
import com.example.ntd.model.OperationTypeEnum;
import com.example.ntd.model.RecordValue;
import com.example.ntd.repository.OperationRepository;
import com.example.ntd.repository.RecordValueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

  @InjectMocks
  private CalculatorService calculatorService;

  @Mock
  private UserService userService;

  @Mock
  private OperationRepository operationRepository;

  @Mock
  private RecordValueRepository recordRepository;

  private UUID userId;
  private String userName;
  private UUID operationId;
  private UserResponse userResponse;
  private PerformOperationRequest operationRequest;
  private Operation operation;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    userName = "testuser";
    operationId = UUID.randomUUID();
    userResponse = new UserResponse(userId, true, BigDecimal.valueOf(1000));
    operationRequest = new PerformOperationRequest(userName, operationId, BigDecimal.valueOf(100));
    operation = new Operation(operationId, OperationTypeEnum.ADDITION, BigDecimal.valueOf(10));
  }

  @Test
  void testPerformOperationSuccessful() {
    when(operationRepository.findById(operationId)).thenReturn(Optional.of(operation));
    when(userService.getUserByUsername(eq("testuser"), anyString())).thenReturn(Mono.just(userResponse));
    when(recordRepository.findFirstByUserIdAndDeletedFalseOrderByDateDesc(userId)).thenReturn(Optional.empty());
    when(recordRepository.save(any(RecordValue.class))).thenReturn(new RecordValue());
    when(userService.updateUserBalance(eq(userId), any(BigDecimal.class), anyString())).thenReturn(Mono.just(userResponse));

    PerformOperationResponse response = calculatorService.performOperation(operationRequest, "token");

    assertNotNull(response);
    assertEquals("100", response.getResult());
    verify(userService, times(1)).updateUserBalance(eq(userId), eq(BigDecimal.valueOf(990)), anyString());
  }

  @Test
  void testPerformOperationInsufficientBalance() {
    userResponse.setBalance(BigDecimal.valueOf(5));
    when(operationRepository.findById(operationId)).thenReturn(Optional.of(operation));
    when(userService.getUserByUsername(eq("testuser"), anyString())).thenReturn(Mono.just(userResponse));

    InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
      calculatorService.performOperation(operationRequest, "token");
    });
  }

  @Test
  void testPerformOperationOperationNotFound() {
    when(operationRepository.findById(operationId)).thenReturn(Optional.empty());

    OperationNotFoundException exception = assertThrows(OperationNotFoundException.class, () -> {
      calculatorService.performOperation(operationRequest, "token");
    });
  }
}