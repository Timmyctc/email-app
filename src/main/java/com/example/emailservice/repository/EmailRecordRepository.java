package com.example.emailservice.repository;

import com.example.emailservice.model.EmailRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRecordRepository extends JpaRepository<EmailRecord, UUID> {
}
