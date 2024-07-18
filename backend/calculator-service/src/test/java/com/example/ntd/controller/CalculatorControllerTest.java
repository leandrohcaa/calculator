package com.example.ntd.controller;

import com.example.ntd.config.TestSecurityConfig;
import com.example.ntd.dto.PerformOperationRequest;
import com.example.ntd.dto.PerformOperationResponse;
import com.example.ntd.exception.InsufficientBalanceException;
import com.example.ntd.exception.OperationNotFoundException;
import com.example.ntd.service.CalculatorService;
import com.example.ntd.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class CalculatorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CalculatorService calculatorService;

  @MockBean
  private OperationService operationService;

  private UUID userId;
  private UUID operationId;
  private PerformOperationRequest operationRequest;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    operationId = UUID.randomUUID();
    operationRequest = new PerformOperationRequest("testuser", operationId, BigDecimal.valueOf(100));
  }

  @Test
  void testPerformOperationSuccessful() throws Exception {
    PerformOperationResponse performOperationResponse = new PerformOperationResponse("1100");
    when(calculatorService.performOperation(any(PerformOperationRequest.class), anyString()))
            .thenReturn(performOperationResponse);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calculator/operations/perform")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"operationId\": \"" + operationId + "\", \"username\": \"testuser\", \"amount\": 100 }"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"result\":\"1100\"}"));
  }

  @Test
  void testPerformOperationInsufficientBalance() throws Exception {
    when(calculatorService.performOperation(any(PerformOperationRequest.class), anyString()))
            .thenThrow(new InsufficientBalanceException("The balance is insufficient. Current balance: 5. Operation cost: 10."));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calculator/operations/perform")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"operationId\": \"" + operationId + "\", \"username\": \"testuser\", \"amount\": 100 }"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("The balance is insufficient. Current balance: 5. Operation cost: 10."));
  }

  @Test
  void testPerformOperationOperationNotFound() throws Exception {
    when(calculatorService.performOperation(any(PerformOperationRequest.class), anyString()))
            .thenThrow(new OperationNotFoundException("Operation not found."));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/calculator/operations/perform")
                    .header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"operationId\": \"" + operationId + "\", \"username\": \"testuser\", \"amount\": 100 }"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Operation not found."));
  }
}