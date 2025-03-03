package es.codeurjc.webapp14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecurityController implements ErrorController {

    @GetMapping("/access-error")
    public String accessError() {
        return "access_error"; 
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "redirect:/no-page-error";
    }

    @GetMapping("/no-page-error")
    public String pageDenied() {
        return "no_page_error";
    }

}