package es.codeurjc.webapp14.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.Order.State;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final OrderService orderService;

    private final UserService userService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @ModelAttribute
    public void addUserAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        boolean isLogged = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);
        model.addAttribute("logged", isLogged);

        if (isLogged) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername()); 
            
            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId()); 
            model.addAttribute("userEmail", user.getEmail());
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("userId", null);
            model.addAttribute("userEmail", null);
        }
    }


    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

     @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
     // To show the register form and handle user registration
     public String registerUser(@ModelAttribute("user") @Valid User user,
             BindingResult result,
             @RequestParam(value = "confirmPassword", defaultValue = "") String confirmPassword,
             Model model, HttpServletRequest request) {

        model.addAttribute("_csrf", request.getAttribute("_csrf"));


        System.out.println("Hago peticion");
         // If the request is GET only show the form
         if (request.getMethod().equals("GET")) {
             System.out.println("Entre en el get");
             model.addAttribute("user", new User());
             return "login_register/register";
         }
         // If the request is POST validate the data
         // Check if fields are empty
         System.out.println("Entre en el post");
         if (user.getName().isEmpty()) {
             result.rejectValue("name", "error.user", "El nombre no puede estar vacío");
         }
         if (user.getSurname().isEmpty()) {
             result.rejectValue("surname", "error.user", "El apellido no puede estar vacío");
         }
         if (user.getEmail().isEmpty()) {
             result.rejectValue("email", "error.user", "El correo electrónico no puede estar vacío");
         }
         if (user.getEncodedPassword().isEmpty()) {
             result.rejectValue("encodedPassword", "error.user", "La contraseña no puede estar vacía");
         }
         if (confirmPassword == null) {
             result.rejectValue("confirmPassword", "error.user",
                     "La confirmación de la contraseña no puede estar vacía");
         }
         // If the email is already registered
         if (userService.findByEmail(user.getEmail()) != null) {
             result.rejectValue("email", "error.user", "Este correo ya está registrado");
         }
         // If the password and password confirmation do not match
         if (!user.getEncodedPassword().equals(confirmPassword)) {
             result.rejectValue("encodedPassword", "error.user", "La contraseña y la confirmación no coinciden");
         }
         // If the password is less than 6 characters
         if (user.getEncodedPassword().length() < 6) {
             result.rejectValue("encodedPassword", "error.user", "La contraseña debe tener al menos 6 caracteres");
         }
         // If there are errors, the view is returned with the messages
         if (result.hasErrors()) {
             System.out.println("Tengo errores");
             model.addAttribute("user", user);
             model.addAttribute("confirmPassword", confirmPassword);
             model.addAttribute("errors", result.getFieldErrors().stream()
                     .collect(Collectors.groupingBy(FieldError::getField,
                             Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
             return "login_register/register";
         }
         // If everything is fine the user is saved
         List<String> roles = new ArrayList<>();
         roles.add("USER");


         String encoded = passwordEncoder.encode(confirmPassword);
         user.setEncodedPassword(encoded);

         user.setRoles(roles);
         userService.saveUser(user);

         Order order = new Order(user,State.No_pagado,false);
         orderService.saveOrder(order);
         // Redirect to another page if all is correct
         return "redirect:/login";
     }
     

     @GetMapping("/login")
     public String showLogin(Model model) {
        return "login_register/login";
     }


    @GetMapping("/user_registered/users_profile")
    public String EditForm(Model model) {
        Long userId = (Long) model.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findById(userId);
        model.addAttribute("user", user);
        
        return "/user_registered/users_profile";
    }

    @PostMapping("/editProfile")
    public String editProfilePost(@ModelAttribute("userId") Long userId, 
                                @RequestParam(required = false) String name, 
                                @RequestParam(required = false) String surname, 
                                @RequestParam(required = false) String email, 
                                @RequestParam(required = false) String address) {

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findById(userId);

        if (name != null) {
            user.setName(name);
        }
        if (surname != null) {
            user.setSurname(surname);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (address != null) {
            user.setAddress(address);
        }

        userService.saveUser(user);

        return "redirect:/index";
    }


    


}
