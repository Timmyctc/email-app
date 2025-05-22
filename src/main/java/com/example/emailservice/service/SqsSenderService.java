package com.example.emailservice.service;

import com.example.emailservice.model.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class SqsSenderService {

    private final ObjectMapper objectMapper;

    private static final String QUEUE_URL = "https://sqs.eu-central-1.amazonaws.com/931424660630/email-queue";

    private final SqsClient sqsClient = SqsClient.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    public void sendEmailRequestToQueue(EmailRequest emailRequest) {
        try {
            String messageBody = objectMapper.writeValueAsString(emailRequest);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message to SQS", e);
        }
    }
}
