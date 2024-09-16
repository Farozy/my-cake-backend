package org.farozy.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OauthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/web/oauth/google")
    public String handleGoogleCallback(@RequestParam("code") String authorizationCode) {
        return authorizationCode;
    }

    @GetMapping("/home")
    public String getToken(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "tokenView";
    }

    @GetMapping("/dashboard")
    public String getDashboard(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "tokenView";
    }

}
