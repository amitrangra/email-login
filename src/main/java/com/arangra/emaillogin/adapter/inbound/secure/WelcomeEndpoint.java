package com.arangra.emaillogin.adapter.inbound.secure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeEndpoint {
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}
