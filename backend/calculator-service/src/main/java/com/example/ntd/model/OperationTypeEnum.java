package com.example.ntd.model;

public enum OperationTypeEnum {
  ADDITION("Addition"),
  SUBTRACTION("Subtraction"),
  MULTIPLICATION("Multiplication"),
  DIVISION("Division"),
  SQUARE_ROOT("Square Root"),
  RANDOM_STRING("Random String");

  private final String label;

  OperationTypeEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}