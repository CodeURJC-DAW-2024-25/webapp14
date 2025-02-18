package es.codeurjc.webapp14.controllers;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    /* 
    @GetMapping("/users/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user.getProfileImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok().headers(headers).body(user.getProfileImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     */

    @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
    // To show the register form and handle user registration
    public String registerUser(@ModelAttribute("user") @Valid User user,
            BindingResult result,
            @RequestParam(value = "confirmPassword", defaultValue = "") String confirmPassword,
            Model model, HttpServletRequest request) {
        // If the request is GET only show the form
        if (request.getMethod().equals("GET")) {
            model.addAttribute("user", new User());
            return "login_register/register";
        }
        // If the request is POST validate the data
        // Check if fields are empty
        if (user.getName().isEmpty()) {
            result.rejectValue("name", "error.user", "El nombre no puede estar vacío");
        }
        if (user.getSurname().isEmpty()) {
            result.rejectValue("surname", "error.user", "El apellido no puede estar vacío");
        }
        if (user.getEmail().isEmpty()) {
            result.rejectValue("email", "error.user", "El correo electrónico no puede estar vacío");
        }
        if (user.getPassword().isEmpty()) {
            result.rejectValue("password", "error.user", "La contraseña no puede estar vacía");
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
        if (!user.getPassword().equals(confirmPassword)) {
            result.rejectValue("password", "error.user", "La contraseña y la confirmación no coinciden");
        }
        // If the password is less than 6 characters
        if (user.getPassword().length() < 6) {
            result.rejectValue("password", "error.user", "La contraseña debe tener al menos 6 caracteres");
        }
        // If there are errors, the view is returned with the messages
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("confirmPassword", confirmPassword);
            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
            return "login_register/register";
        }
        // If everything is fine the user is saved
        user.setRole(User.Role.CUSTOMER);
        userService.saveUser(user);
        // Redirect to another page if all is correct
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    // To show the login form and handle the user login process
    public String loginUser(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            Model model, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        // If the request is GET only show the form
        if (request.getMethod().equals("GET")) {
            model.addAttribute("user", new User());
            return "login_register/login";
        }
        // The user is searched by email
        User user = userService.findByEmail(email);
        // Check if the user exists
        if (user == null) {
            errors.put("emailError", "El correo electrónico no está registrado");
        } // Check if the password is incorrect
        else if (!user.getPassword().equals(password)) {
            errors.put("passwordError", "Contraseña incorrecta");
        }
        // If there are errors, the view is returned with the messages
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("email", email);
            return "login_register/login";
        }
        // Redirect to another page if credentials are correct
        return "redirect:/admin/profile"; // CHANGE THIS
    }
    
}
