package com.example.ntd.service;

import com.example.ntd.dto.PerformOperationRequest;
import com.example.ntd.dto.PerformOperationResponse;
import com.example.ntd.dto.RecordResponse;
import com.example.ntd.dto.UserResponse;
import com.example.ntd.exception.InsufficientBalanceException;
import com.example.ntd.exception.OperationNotFoundException;
import com.example.ntd.exception.RecordNotFoundException;
import com.example.ntd.mapper.RecordValueMapper;
import com.example.ntd.model.Operation;
import com.example.ntd.model.OperationTypeEnum;
import com.example.ntd.model.RecordValue;
import com.example.ntd.repository.OperationRepository;
import com.example.ntd.repository.RecordValueRepository;
import com.example.ntd.util.BigDecimalValidator;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CalculatorService {

  @Autowired
  private UserService userService;

  @Autowired
  private OperationRepository operationRepository;

  @Autowired
  private RecordValueRepository recordRepository;

  @Autowired
  private RecordValueMapper recordValueMapper;

  @Transactional
  public PerformOperationResponse performOperation(PerformOperationRequest request, String token) {
    Operation operation = operationRepository.findById(request.getOperationId())
            .orElseThrow(() -> new OperationNotFoundException("Operation not found."));

    UserResponse user = userService.getUserByUsername(request.getUsername(), token).block();
    BigDecimal newBalance = user.getBalance().subtract(operation.getCost());
    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
      throw new InsufficientBalanceException(String.format("The balance is insufficient. " +
              "Current balance: %s. Operation cost: %s.", user.getBalance().toPlainString(), operation.getCost().toPlainString()));
    }

    Optional<RecordValue> recordValue = recordRepository.findFirstByUserIdAndDeletedFalseOrderByDateDesc(user.getId());
    BigDecimal initialAmount = BigDecimal.ZERO;
    if (recordValue.isPresent() && BigDecimalValidator.isValidBigDecimal(recordValue.get().getOperationResponse())) {
      initialAmount = new BigDecimal(recordValue.get().getOperationResponse());
    }

    String response = performCalculation(initialAmount, request.getAmount(), operation.getType());
    RecordValue record = new RecordValue();
    record.setId(UUID.randomUUID());
    record.setOperation(operation);
    record.setUserId(user.getId());
    record.setAmount(request.getAmount());
    record.setUserBalance(newBalance);
    record.setOperationResponse(response);
    record.setDate(LocalDateTime.now());
    recordRepository.save(record);

    userService.updateUserBalance(user.getId(), newBalance, token).block();

    return new PerformOperationResponse(response);
  }

  private String performCalculation(BigDecimal initialAmount, BigDecimal amount, OperationTypeEnum type) {
    switch (type) {
      case ADDITION:
        return initialAmount.add(amount).toString();
      case SUBTRACTION:
        return initialAmount.subtract(amount).toString();
      case MULTIPLICATION:
        return initialAmount.multiply(amount).toString();
      case DIVISION:
        if (amount.equals(BigDecimal.ZERO)) throw new ArithmeticException("Cannot divide by zero.");
        return initialAmount.divide(amount).toString();
      case SQUARE_ROOT:
        if (initialAmount.compareTo(BigDecimal.ZERO) < 0) throw new ArithmeticException("Cannot take square root of negative number.");
        return BigDecimal.valueOf(Math.sqrt(initialAmount.doubleValue())).toString();
      case RANDOM_STRING:
        return RandomStringUtils.random(10);
      default:
        throw new IllegalArgumentException("Invalid operation type.");
    }
  }

  public Page<RecordResponse> getRecords(UUID userId, int page, int size, String search, String sortField, String sortDirection) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
    Page<RecordValue> recordValues;
    if (search == null || search.isEmpty()) {
      recordValues = recordRepository.findByUserIdAndDeletedFalse(userId, pageable);
    } else {
      recordValues = recordRepository.findByUserIdAndDeletedFalseAndOperationResponseContaining(userId, search, pageable);
    }
    return recordValues.map(recordValueMapper::toDto);
  }

  @Transactional
  public void deleteRecord(UUID recordId) {
    RecordValue recordValue = recordRepository.findById(recordId)
            .orElseThrow(() -> new RecordNotFoundException("Record not found"));
    recordValue.setDeleted(true);
    recordRepository.save(recordValue);
  }
}