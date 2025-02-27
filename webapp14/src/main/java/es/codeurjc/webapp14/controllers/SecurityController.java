package es.codeurjc.webapp14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/access-error")
    public String accessError() {
        return "access_error"; 
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_error";
    }

    @GetMapping("/no-page-error")
    public String pageDenied() {
        return "no_page_error";
    }
}