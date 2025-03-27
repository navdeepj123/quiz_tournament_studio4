package com.example.quiz_tournament.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vikash20032103@gmail.com"); // ✅ Your correct Gmail
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            System.out.println("✅ Email sent to: " + toEmail); // ✅ Log success
        } catch (Exception e) {
            System.out.println("❌ Email failed to " + toEmail + ": " + e.getMessage()); // ✅ Log failure
        }
    }
}
