package com.example.ntd.mapper;

import com.example.ntd.dto.RecordResponse;
import com.example.ntd.model.RecordValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecordValueMapper {

  @Mapping(source = "operation.id", target = "operationId")
  RecordResponse toDto(RecordValue recordValue);
}