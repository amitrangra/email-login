package com.arangra.emaillogin.domain;

import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

import java.time.LocalDateTime;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtService {
    private JWSHeader jwsHeader;
    private JWSSigner signer;

    public JwtService(JWSHeader jwsHeader, JWSSigner signer) {
        this.jwsHeader = jwsHeader;
        this.signer = signer;
    }

    public String generateJwt(String email) throws JOSEException {
        JWTClaimsSet claimsSet = toJwtClaims(email);
        SignedJWT signedJWT = sign(claimsSet);
        return signedJWT.serialize();
    }

    private JWTClaimsSet toJwtClaims(String email) {
        LocalDateTime now = LocalDateTime.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("https://example.com")
                .issueTime(from(now.atZone(systemDefault()).toInstant()))
                .expirationTime(from(now.plusHours(4).atZone(systemDefault()).toInstant()))
                .build();
        return claimsSet;
    }

    private SignedJWT sign(JWTClaimsSet claimsSet) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }
}
