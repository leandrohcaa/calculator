package com.example.ntd.service;

import com.example.ntd.dto.OperationResponse;
import com.example.ntd.model.Operation;
import com.example.ntd.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {

  @Autowired
  private OperationRepository operationRepository;

  public List<OperationResponse> getAllOperations() {
    return operationRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

  private OperationResponse convertToDto(Operation operation) {
    return new OperationResponse(operation.getId(), operation.getType().getLabel());
  }
}