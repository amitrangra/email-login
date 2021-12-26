package com.arangra.emaillogin.adapter.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.arangra.emaillogin.adapter.outbound.email.EmailClient;

@Configuration
public class OutboundAdapterConfig {
    @Autowired
    private JavaMailSender emailSender;

    @Bean
    public EmailClient emailClient() {
        return new EmailClient(emailSender);
    }
}
