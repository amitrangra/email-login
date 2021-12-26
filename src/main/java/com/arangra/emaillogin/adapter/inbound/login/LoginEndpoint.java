package com.arangra.emaillogin.adapter.inbound.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.arangra.emaillogin.domain.LoginService;
import com.nimbusds.jose.JOSEException;

@Controller
public class LoginEndpoint {
    private LoginService loginService;

    public LoginEndpoint(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping ("/login")
    public String page() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="email", required=false, defaultValue="World@heaven.com") String email, Model model) throws JOSEException {
        model.addAttribute("email", email);
        loginService.sendEmail(email);
        return "greeting";
    }
}
