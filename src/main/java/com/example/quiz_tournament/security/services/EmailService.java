// src/main/java/com/example/quiz_tournament/services/EmailService.java
package com.example.quiz_tournament.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendTournamentNotification(String toEmail, String tournamentName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("New Quiz Tournament: " + tournamentName);
        message.setText("A new tournament '" + tournamentName + "' is available!");

        mailSender.send(message);
    }
}