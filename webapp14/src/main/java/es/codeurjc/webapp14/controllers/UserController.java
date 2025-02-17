package es.codeurjc.webapp14.controllers;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.UserService;
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

    @GetMapping("/register") // To show the register form
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "login_register/register";
    }

    @PostMapping("/register/user") // To handle user registration
    public String registerUser(@ModelAttribute("user") @Valid User user,
            BindingResult result,
            @RequestParam(value = "confirmPassword", defaultValue = "") String confirmPassword,
            Model model) {
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

    @GetMapping("/login") // To show the login form
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login_register/login";
    }

    @PostMapping("/login/user") // To handle the user login process
    public String loginUser(@RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        Map<String, String> errors = new HashMap<>();
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
            model.addAttribute("email", email); // Mantener el email ingresado
            return "login_register/login"; // Asegúrate de que el nombre de la vista coincide con tu plantilla HTML
        }
        // Redirect to another page if credentials are correct
        return "redirect:/admin/profile"; // CHANGE THIS
    }

}
