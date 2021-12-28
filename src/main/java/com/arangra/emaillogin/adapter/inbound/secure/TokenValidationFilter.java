package com.arangra.emaillogin.adapter.inbound.secure;

import static com.nimbusds.jwt.SignedJWT.parse;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;

public class TokenValidationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(TokenValidationFilter.class);
    private JWSVerifier jwsVerifier;

    public TokenValidationFilter(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        LOG.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        if(request.getParameter("token") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "The token is not present");
        } else if (! verifyToken(request.getParameter("token"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid");
        } else {
            filterChain.doFilter(request, response);
        }

        LOG.info("Logging Response :{}", response.getContentType());
    }

    private boolean verifyToken(String token) {
        try {
            SignedJWT signedJWT = parse(token);
            LOG.info("Subject {}", signedJWT.getJWTClaimsSet().getSubject());
            LOG.info("Issuer {}", signedJWT.getJWTClaimsSet().getIssuer());
            LOG.info("Issue Time {}", signedJWT.getJWTClaimsSet().getIssueTime());
            LOG.info("Expiry {}", signedJWT.getJWTClaimsSet().getExpirationTime());
            return signedJWT.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            LOG.warn("Token Validation exception {}", e.getMessage());
            return false;
        }
    }
}
