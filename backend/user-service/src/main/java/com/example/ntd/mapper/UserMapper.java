package com.example.ntd.mapper;

import com.example.ntd.dto.UserResponse;
import com.example.ntd.model.User;
import com.example.ntd.model.UserStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "status", target = "isActive", qualifiedByName = "statusToBoolean")
  UserResponse toDto(User user);

  @Named("statusToBoolean")
  default Boolean statusToBoolean(UserStatusEnum status) {
    return status == UserStatusEnum.ACTIVE;
  }
}