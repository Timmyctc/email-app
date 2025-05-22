package com.example.emailservice.controller;

import com.example.emailservice.model.EmailRecord;
import com.example.emailservice.model.EmailRequest;
import com.example.emailservice.repository.EmailRecordRepository;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.SqsSenderService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailRecordRepository repository;
    private final SqsSenderService senderService;

    @PostMapping("/queue")
    public ResponseEntity<String> queueEmail(@RequestBody @Valid EmailRequest request) {
        senderService.sendEmailRequestToQueue(request);
        return  ResponseEntity.ok("Email successfully sent to queue!");
    }

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequest request) {
         emailService.sendEmail(request);
         return ResponseEntity.ok("Email Sent!");
    }

    @PostMapping("/html")
    public ResponseEntity<String> sendHtmlEmail(@RequestBody @Valid EmailRequest request) throws MessagingException {
         emailService.sendHtmlEmail(request);
         return ResponseEntity.ok("Email Sent!");
    }

    @GetMapping
    public List<EmailRecord> emailList() {
        return repository.findAll();
    }

}
