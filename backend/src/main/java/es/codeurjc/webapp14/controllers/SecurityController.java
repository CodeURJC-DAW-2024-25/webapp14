package es.codeurjc.webapp14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

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
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/no-page-error");
    }

    @GetMapping("/no-page-error")
    public String nonexistentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        boolean logged = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .anyMatch(role -> role.equals("ROLE_USER"));

        model.addAttribute("logged", logged);

        return "no_page_error"; 
    }

}