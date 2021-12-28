package com.arangra.emaillogin.domain;

import static com.arangra.emaillogin.adapter.inbound.login.LoginEndpoint.GOTO;
import static java.lang.String.format;

import org.springframework.mail.SimpleMailMessage;

import com.arangra.emaillogin.port.EmailClientPort;
import com.nimbusds.jose.JOSEException;

public class LoginService {
    private EmailClientPort emailClient;
    private SimpleMailMessage mailMessage;
    private JwtService jwtService;

    public LoginService(EmailClientPort emailClient, SimpleMailMessage mailMessage, JwtService jwtService) {
        this.emailClient = emailClient;
        this.mailMessage = mailMessage;
        this.jwtService = jwtService;
    }

    public String sendEmail(String to, String gotoPage) throws JOSEException {
        String token = jwtService.generateJwt(to);
        String gotoPageWithToken = GOTO.equals(gotoPage)? gotoPage:gotoPage+"?token="+token;
        mailMessage.setText(format(mailMessage.getText(), token, gotoPageWithToken));
        mailMessage.setTo(to);
        emailClient.sendEmail(mailMessage);
        return "success";
    }
}
