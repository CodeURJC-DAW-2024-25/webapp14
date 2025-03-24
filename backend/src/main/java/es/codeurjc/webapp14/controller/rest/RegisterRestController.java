package es.codeurjc.webapp14.controller.rest;


import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.webapp14.dto.NewUserDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")

public class RegisterRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isLogged = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);

        model.addAttribute("logged", isLogged);

        if (isLogged) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());

            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId());
            model.addAttribute("admin", user.getRoles().contains("ADMIN"));
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("userId", null);
            model.addAttribute("admin", false);
        }
    }



    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@RequestBody NewUserDTO newUserDTO,
            BindingResult result,
            Model model) {

        userService.validateNewUser(newUserDTO, result);

        if (result.hasErrors()) {
            throw new AccessDeniedException("User invalid");
        }

        try {
            String encodedPassword = passwordEncoder.encode(newUserDTO.encodedPassword());
            UserDTO newUser = userService.registerUser(newUserDTO, encodedPassword);
            orderService.listProducts(newUser.id());
            URI location = URI.create("https://localhost:8443/api/v1/users/" + newUser.id());
            return ResponseEntity.created(location).body(newUser);
        } catch (IllegalArgumentException e) {
           throw new AccessDeniedException("User invalid");
        }
    }

    @PostMapping("/image")
    public ResponseEntity<Object> createUserImage(@ModelAttribute("userId") long userId,
                                                    @RequestParam MultipartFile imageFile) throws IOException {

        URI location = URI.create("https://localhost:8443/api/v1/users/image");

        userService.createUserImage(userId, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();
    }

}
