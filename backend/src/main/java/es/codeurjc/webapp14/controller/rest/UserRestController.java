package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.EditUserDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDTO> getUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return users;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id, @ModelAttribute("userId") long userId) {

        if (userId != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/image")
    public ResponseEntity<Object> getUserImage(@ModelAttribute("userId") long userId) throws SQLException, IOException {

        Resource userImage = userService.getUserImage(userId);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(userImage);
    }

    @GetMapping("/admin")
    public ResponseEntity<UserDTO> getAdmin(@ModelAttribute("admin") boolean isAdmin) {

        User admin = userService.getAdmin().orElse(null);

        if (admin == null) {
            return ResponseEntity.notFound().build();
        } else if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(userMapper.toDTO(admin));
    }

    /*
     * @GetMapping("/email/{email}")
     * public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
     * User user = userService.findByEmail(email);
     * 
     * if (user == null) {
     * return ResponseEntity.notFound().build();
     * }
     * 
     * return ResponseEntity.ok(userMapper.toDTO(user));
     * }
     */

    @PutMapping()
    public UserDTO editUser(@ModelAttribute("userId") Long userId,
            @RequestBody EditUserDTO editUserDTO,
            RedirectAttributes redirectAttributes) {

        Optional<UserDTO> userConsult = Optional.ofNullable(userService.findById(userId));

        if (!userConsult.isPresent()) {
            return null;
        }

        User user = userService.findUserById(userId);

        if (!userService.validateEditUser(editUserDTO, user, redirectAttributes)) {
            return null;
        }

        try {
            String encodedPassword = passwordEncoder.encode(editUserDTO.newPassword());
            UserDTO editUser = userService.updateUserFromDTO(user, editUserDTO, encodedPassword);
            return editUser;
        } catch (RuntimeException e) {
            return null;
        }
    }

    @PutMapping("/image")
    public ResponseEntity<Object> replaceUserImage(@ModelAttribute("userId") long userId,
            @RequestParam MultipartFile imageFile) throws IOException {

        userService.replaceUserImage(userId, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping()
    public UserDTO deleteUser(@ModelAttribute("userId") long userId) {
        UserDTO user = userService.findById(userId);
        userService.delete(userId);
        return user;
    }

    @DeleteMapping("/image")
    public ResponseEntity<Object> deleteUserImage(@ModelAttribute("userId") long userId) throws IOException {

        userService.deleteUserImage(userId);

        return ResponseEntity.noContent().build();
    }
    
}
