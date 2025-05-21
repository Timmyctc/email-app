package com.example.emailservice.service;

import com.example.emailservice.model.EmailRecord;
import com.example.emailservice.model.EmailRequest;
import com.example.emailservice.repository.EmailRecordRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailRecordRepository repository;
    private final JavaMailSenderImpl mailSender;

    @Async
    public EmailRecord sendEmail(EmailRequest emailRequest) {

        //For Saving to DB
        EmailRecord record = EmailRecord.builder()
                .recipient(emailRequest.getRecipient())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .build();

        try {
            //For Sending to Mail Server
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getRecipient());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getBody());
            javaMailSender.send(message);
            record.setStatus("SENT");
        }
        catch (Exception e) {
            record.setStatus("FAILED");
        }

        //Save to DB
        return repository.save(record);
    }

    public void sendHtmlEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        EmailRecord record = EmailRecord.builder()
                .recipient(emailRequest.getRecipient())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .timestamp(LocalDateTime.now())
                .status("PENDING")
                .build();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailRequest.getRecipient());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getBody(), true);
            message.saveChanges();
            javaMailSender.send(message);
            record.setStatus("SENT");
        } catch (Exception e) {
            record.setStatus("FAILED");
        }
    }
}
