package smallITgroup.accounting.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender; // Mail sender instance for sending emails

    // Constructor injection of JavaMailSender
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender; // Initializing the mail sender
    }

    // Method to send an email
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); // Create a simple mail message
        message.setTo(to); // Set the recipient email address
        message.setSubject(subject); // Set the subject of the email
        message.setText(text); // Set the body of the email
        mailSender.send(message); // Send the email using the mailSender
    }
}