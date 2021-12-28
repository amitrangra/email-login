package com.arangra.emaillogin.port;

import org.springframework.mail.SimpleMailMessage;

public interface EmailClientPort {
    void sendEmail(SimpleMailMessage mailMessage);
}
