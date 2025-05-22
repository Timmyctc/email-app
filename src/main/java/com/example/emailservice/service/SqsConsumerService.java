package com.example.emailservice.service;

import com.example.emailservice.model.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsConsumerService {

    private static final Integer MAX_NUMBER_MESSAGES = 5;
    private static final Integer WAIT_TIME_SECONDS = 10;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    private final SqsClient sqsClient = SqsClient.builder()
            .region(Region.EU_CENTRAL_1)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    private final static String QUEUE_URL = "https://sqs.eu-central-1.amazonaws.com/931424660630/email-queue";


    @PostConstruct
    public void startPolling() {
        new Thread(() -> {
            while (true) {
                try {
                    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                            .queueUrl(QUEUE_URL)
                            .maxNumberOfMessages(MAX_NUMBER_MESSAGES)
                            .waitTimeSeconds(WAIT_TIME_SECONDS)
                            .build();

                    List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

                    for (Message message : messages) {
                        try {
                            EmailRequest emailRequest = objectMapper.readValue(message.body(), EmailRequest.class);
                            log.info("Processing Email: {}", emailRequest);

                            //Additional Logic here
                            emailService.sendEmail(emailRequest);


                            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                                    .queueUrl(QUEUE_URL)
                                    .receiptHandle(message.receiptHandle())
                                    .build());
                        } catch (Exception e) {
                            log.error("ERROR processing message : {}", message.body(), e);
                        }
                    }
                } catch (Exception e) {
                    log.error("ERROR receiving message from SQS", e);
                }
            }
        }).start();
    }
}
