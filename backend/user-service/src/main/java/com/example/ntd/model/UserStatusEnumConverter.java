package com.example.ntd.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusEnumConverter implements AttributeConverter<UserStatusEnum, Boolean> {

  @Override
  public Boolean convertToDatabaseColumn(UserStatusEnum attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getValue();
  }

  @Override
  public UserStatusEnum convertToEntityAttribute(Boolean dbData) {
    if (dbData == null) {
      return null;
    }
    return UserStatusEnum.fromValue(dbData);
  }
}