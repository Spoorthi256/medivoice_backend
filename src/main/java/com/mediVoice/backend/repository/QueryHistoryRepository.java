package com.mediVoice.backend.repository;

import com.mediVoice.backend.model.QueryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryHistoryRepository extends JpaRepository<QueryHistory, Long> {
    List<QueryHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<QueryHistory> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}