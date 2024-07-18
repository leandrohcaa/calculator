package com.example.ntd.repository;

import com.example.ntd.model.RecordValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordValueRepository extends JpaRepository<RecordValue, UUID> {

  Optional<RecordValue> findFirstByUserIdAndDeletedFalseOrderByDateDesc(UUID userId);

  Page<RecordValue> findByUserIdAndDeletedFalse(UUID userId, Pageable pageable);

  Page<RecordValue> findByUserIdAndDeletedFalseAndOperationResponseContaining(UUID userId, String search, Pageable pageable);

}