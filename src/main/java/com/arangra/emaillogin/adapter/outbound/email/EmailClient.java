package com.arangra.emaillogin.adapter.outbound.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.arangra.emaillogin.port.EmailClientPort;

public class EmailClient implements EmailClientPort {
    private JavaMailSender emailSender;

    public EmailClient(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(SimpleMailMessage mailMessage) {
        emailSender.send(mailMessage);
    }
}
