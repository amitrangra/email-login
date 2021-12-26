package com.arangra.emaillogin.domain;

import static com.nimbusds.jose.JWSAlgorithm.ES384;

import java.text.ParseException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

import com.arangra.emaillogin.port.EmailClientPort;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;

@Configuration
public class DomainConfig {
    @Value("${jwk}")
    private String jwk;

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    public EmailClientPort emailClient;

    @Bean
    public LoginService loginService(SimpleMailMessage mailMessage, JwtService jwtService) {
        return new LoginService(emailClient, mailMessage, jwtService);
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("Email Login Token @"+ LocalDateTime.now());
        message.setText("Token: %s \n Link: %s");
        return message;
    }

    @Bean
    public ECKey ecKey() {
        try {
            return (ECKey) JWK.parse(jwk);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JWSHeader jwsHeader(ECKey ecKey) {
        return new Builder(ES384).keyID(ecKey.getKeyID()).build();
    }

    @Bean
    public JWSSigner jwsSigner(ECKey ecKey) {
        try {
            return new ECDSASigner(ecKey);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JWSVerifier jwsVerifier(ECKey ecKey) {
        try {
            return new ECDSAVerifier(ecKey.toPublicJWK());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JwtService jwtService(JWSHeader jwsHeader, JWSSigner jwsSigner) {
        return new JwtService(jwsHeader, jwsSigner);
    }
}
