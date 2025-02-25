package es.codeurjc.webapp14.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
     * @GetMapping("/users/image/{id}")
     * 
     * @ResponseBody
     * public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
     * User user = userService.getUserById(id);
     * if (user.getProfileImage() != null) {
     * HttpHeaders headers = new HttpHeaders();
     * headers.setContentType(MediaType.IMAGE_JPEG);
     * return ResponseEntity.ok().headers(headers).body(user.getProfileImage());
     * } else {
     * return ResponseEntity.notFound().build();
     * }
     * }
     */

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
         
         // Redirect to another page if all is correct
         return "redirect:/login";
     }
     

    @Controller
    @RequestMapping("/login")
    public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String loginUser(@RequestParam(value = "email", required = false) String email,
                            @RequestParam(value = "password", required = false) String password,
                            Model model, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getMethod().equals("GET")) {
            model.addAttribute("user", new User());
            return "login_register/login";
        }

        User user = userService.findByEmail(email);


        if (user == null) {
            errors.put("emailError", "El correo electrónico no está registrado");
        } else if (!passwordEncoder.matches(password, user.getEncodedPassword())) {
            errors.put("passwordError", "Contraseña incorrecta");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("email", email);
            return "login_register/login";
        }

        request.getSession().invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("logged", true);
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userId", user.getId());
        session.setAttribute("admin", user.getRoles().contains("ADMIN"));

        return user.getRoles().contains("ADMIN") ? "redirect:/admin/profile" : "redirect:/index";
    }
}


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/index";
    }


}
