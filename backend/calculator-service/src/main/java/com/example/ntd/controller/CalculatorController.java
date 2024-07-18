package com.example.ntd.controller;

import com.example.ntd.dto.OperationResponse;
import com.example.ntd.dto.PerformOperationRequest;
import com.example.ntd.dto.RecordResponse;
import com.example.ntd.exception.InsufficientBalanceException;
import com.example.ntd.exception.OperationNotFoundException;
import com.example.ntd.service.CalculatorService;
import com.example.ntd.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/calculator")
@Tag(name = "Operations", description = "API for managing calculator operations")
public class CalculatorController {

  @Autowired
  private CalculatorService calculatorService;

  @Autowired
  private OperationService operationService;

  @PostMapping("/operations/perform")
  @Operation(summary = "Perform operation", description = "Perform a new calculator operation")
  public ResponseEntity<?> performOperation(@RequestBody PerformOperationRequest request,
                                            @RequestHeader("Authorization") String token) {
    try {
      return ResponseEntity.ok(calculatorService.performOperation(request, token));
    } catch (InsufficientBalanceException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (OperationNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/records")
  @Operation(summary = "Get records", description = "Get a list of records")
  public ResponseEntity<Page<RecordResponse>> getRecords(
          @RequestParam(value = "user_id") UUID userId,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size,
          @RequestParam(value = "search", required = false) String search,
          @RequestParam(value = "sortField", defaultValue = "date") String sortField,
          @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection
  ) {
    return ResponseEntity.ok(calculatorService.getRecords(userId, page, size, search, sortField, sortDirection));
  }

  @DeleteMapping("/records/{recordId}")
  @Operation(summary = "Delete record", description = "Logically delete a record by marking it as deleted")
  public ResponseEntity<Void> deleteRecord(@PathVariable UUID recordId) {
    calculatorService.deleteRecord(recordId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/operations")
  public List<OperationResponse> getAllOperations() {
    return operationService.getAllOperations();
  }
}