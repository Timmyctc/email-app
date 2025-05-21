package com.example.emailservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    @Email
    @NotBlank(message = "Recipient is Required")
    private String recipient;
    @NotBlank(message = "Subject is Required")
    private String subject;
    private String body;
}
