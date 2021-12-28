package com.arangra.emaillogin.adapter.inbound.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.arangra.emaillogin.domain.LoginService;
import com.nimbusds.jose.JOSEException;

@Controller
public class LoginEndpoint {
    public static final String GOTO = "None Provided";
    private LoginService loginService;

    public LoginEndpoint(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping ("/login")
    public String page(@RequestParam(name="goto", required=false, defaultValue=GOTO) String gotoPage,
                       Model model) {
        model.addAttribute("goto", gotoPage);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name="email", required=false, defaultValue="World@heaven.com") String email,
                        @RequestParam(name="goto", required=false, defaultValue=GOTO) String gotoPage,
                        Model model) throws JOSEException {
        model.addAttribute("email", email);
        loginService.sendEmail(email, gotoPage);
        return "loginSuccess";
    }
}
