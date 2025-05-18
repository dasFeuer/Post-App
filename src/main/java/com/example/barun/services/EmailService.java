package com.example.barun.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

public interface EmailService {
    void sendEmail(String to, String subject, String text) throws MessagingException;
}
