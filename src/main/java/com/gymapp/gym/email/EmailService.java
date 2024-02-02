package com.gymapp.gym.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private Queue<SimpleMailMessage> emailQueue = new LinkedList<>();

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    public void addToEmailQueue(String to, String subject, String text) {
        if (to == null || subject == null || text == null) {
            throw new IllegalArgumentException("Got null values for email service");
        }

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);

        emailQueue.offer(email);
    }

    public void sendNextEmailInQueue() {
        if (!emailQueue.isEmpty()) {
            SimpleMailMessage nextEmail = emailQueue.peek();
            try {
                javaMailSender.send(nextEmail);
                emailQueue.poll();
                log.info("Sent email: {}", nextEmail);
            } catch (MailException e) {
                log.error("Failed to send email: {}", e.getMessage());

            }
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendQueuedEmails() {
        int batchSize = 5;
        for (int i = 0; i < batchSize; i++) {
            if (emailQueue.isEmpty()) {
                break;
            }
            sendNextEmailInQueue();
        }
    }
}
