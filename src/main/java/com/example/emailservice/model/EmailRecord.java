package com.example.emailservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String recipient;
    private String subject;
    private String body;
    private String status;
    private LocalDateTime timestamp;
}
