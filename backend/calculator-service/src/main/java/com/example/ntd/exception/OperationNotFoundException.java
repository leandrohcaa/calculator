package com.example.ntd.exception;

public class OperationNotFoundException extends RuntimeException {
  public OperationNotFoundException(String message) {
    super(message);
  }
}