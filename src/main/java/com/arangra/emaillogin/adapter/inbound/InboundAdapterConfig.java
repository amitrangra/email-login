package com.arangra.emaillogin.adapter.inbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.arangra.emaillogin.adapter.inbound.login.LoginEndpoint;
import com.arangra.emaillogin.adapter.inbound.secure.TokenValidationFilter;
import com.arangra.emaillogin.adapter.inbound.secure.WelcomeEndpoint;
import com.arangra.emaillogin.domain.LoginService;
import com.nimbusds.jose.JWSVerifier;

@Configuration
public class InboundAdapterConfig {
    @Autowired
    private LoginService loginService;

    @Bean
    public LoginEndpoint greetingEndpoint() {
        return new LoginEndpoint(loginService);
    }

    @Bean
    public WelcomeEndpoint welcomeEndpoint() {
        return new WelcomeEndpoint();
    }

    @Bean
    public FilterRegistrationBean <TokenValidationFilter> filterRegistrationBean(JWSVerifier jwsVerifier) {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean();
        TokenValidationFilter tokenValidationFilter = new TokenValidationFilter(jwsVerifier);
        registrationBean.setFilter(tokenValidationFilter);
        registrationBean.addUrlPatterns("/welcome");
        registrationBean.setOrder(-1);
        return registrationBean;
    }
}
