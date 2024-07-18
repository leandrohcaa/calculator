package com.example.ntd.model;

public enum UserStatusEnum {
  ACTIVE(true),
  INACTIVE(false);

  private final boolean value;

  UserStatusEnum(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  public static UserStatusEnum fromValue(boolean value) {
    return value ? ACTIVE : INACTIVE;
  }
}