package es.codeurjc.webapp14.controller.web;

import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.dto.NewUserDTO;
import es.codeurjc.webapp14.dto.EditUserDTO;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {


    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, OrderService orderService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    // ------------------------------ Add attributes ------------------------------

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

    // ------------------------------ Register user ------------------------------

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("userDTO", new NewUserDTO("", "", "", "", ""));

        return "login_register/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") @Valid NewUserDTO newUserDTO,
            BindingResult result,
            Model model) {

        userService.validateNewUser(newUserDTO, result);

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
            return "login_register/register";
        }

        try {
            String encodedPassword = passwordEncoder.encode(newUserDTO.encodedPassword());
            UserDTO newUser = userService.registerUser(newUserDTO, encodedPassword);
            orderService.listProducts(newUser.id());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login_register/register";
        }

        return "redirect:/login";
    }

    // ------------------------------ Login user ------------------------------

    @GetMapping("/login")
    public String showLogin(Model model) {

        System.out.println("LOGIN");

        return "login_register/login";
    }

    // ------------------------------ Edit user ------------------------------

    @GetMapping("/user_registered/users_profile")
    public String editForm(Model model) {

        Long userId = (Long) model.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        UserDTO userDTO = userService.findById(userId);

        if (userDTO == null) {
            return "redirect:/no-page-error";
        }

        model.addAttribute("user", userDTO);

        return "/user_registered/users_profile";
    }

    @PostMapping("/editProfile")
    public String editProfilePost(@ModelAttribute("userId") Long userId,
            @ModelAttribute("editProfileDTO") EditUserDTO editUserDTO,
            RedirectAttributes redirectAttributes) {

        if (userId == null) {
            return "redirect:/login";
        }

        Optional<UserDTO> userConsult = Optional.ofNullable(userService.findById(userId));

        if (!userConsult.isPresent()) {
            return "redirect:/no-page-error";
        }

        User user = userService.findUserById(userId);

        if (userService.validateEditUser(editUserDTO, user, redirectAttributes)) {
            redirectAttributes.addFlashAttribute("editProfileDTO", editUserDTO);
            return "redirect:/user_registered/users_profile";
        }

        try {
            String encodedPassword = passwordEncoder.encode(editUserDTO.newPassword());

            userService.updateUserFromDTO(user, editUserDTO, encodedPassword);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorImage", e.getMessage());
        }

        return "redirect:/index";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@ModelAttribute("userId") long userId, HttpServletRequest request, HttpServletResponse response) {
        
        userService.findById(userId);
        userService.delete(userId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/index";
    }


    // ------------------------------ List users ------------------------------

    @GetMapping
    public String listUsers(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "users";
    }

}