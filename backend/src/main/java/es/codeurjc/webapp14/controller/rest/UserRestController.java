package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.EditUserDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for managing Users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

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

    @Operation(summary = "Get Users", description = "Return all the Users created")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated users
        data.put("users", userService.getUsersPaginated(page, size));

        // Get total users
        data.put("totalUsers", userService.getTotalUsers());

        // Get pending reports. This function does not exist, we have to look at how its
        // done on the web controller
        
        
        //data.put("pendingReports", userService.getPendingReports());

        // Get banned users count
        data.put("bannedUsers", userService.getAllUsersBanned());

        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Get User", description = "Return a single User")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id, @ModelAttribute("userId") long userId) {

        if (userId != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserDTO user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get User Image", description = "Return a single User Image")
    @GetMapping("/image")
    public ResponseEntity<Object> getUserImage(@ModelAttribute("userId") long userId) throws SQLException, IOException {

        Resource userImage = userService.getUserImage(userId);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(userImage);
    }

    @Operation(summary = "Get Admin", description = "Return Admin information")
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

    @Operation(summary = "Edit User", description = "Edit the actual User")
    @PutMapping()
    public UserDTO editUser(@ModelAttribute("userId") Long userId,
            @RequestBody EditUserDTO editUserDTO,
            RedirectAttributes redirectAttributes) {

        Optional<UserDTO> userConsult = Optional.ofNullable(userService.findById(userId));

        if (!userConsult.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        User user = userService.findUserById(userId);

        if (!userService.validateEditUser(editUserDTO, user, redirectAttributes)) {
            throw new EntityNotFoundException("User not found");
        }

        try {
            String encodedPassword = passwordEncoder.encode(editUserDTO.newPassword());
            UserDTO editUser = userService.updateUserFromDTO(user, editUserDTO, encodedPassword);
            return editUser;
        } catch (RuntimeException e) {
            throw new EntityNotFoundException("User not found");
        }
    }

    @Operation(summary = "Edit User Image", description = "Edit the actual User Image")
    @PutMapping("/image")
    public ResponseEntity<Object> replaceUserImage(@ModelAttribute("userId") long userId,
            @RequestParam MultipartFile imageFile) throws IOException {

        userService.replaceUserImage(userId, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "Delete User", description = "Delete the actual User")
    @DeleteMapping()
    public UserDTO deleteUser(@ModelAttribute("userId") long userId) {
        UserDTO user = userService.findById(userId);
        userService.delete(userId);
        return user;
    }

    @Operation(summary = "Delete User Image", description = "Delete the actual User Image")
    @DeleteMapping("/image")
    public ResponseEntity<Object> deleteUserImage(@ModelAttribute("userId") long userId) throws IOException {

        userService.deleteUserImage(userId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ban/Unban User", description = "Update a User to banned/unbanned")
    @PatchMapping("/{id}")
    public UserDTO ban_unbanUser(@PathVariable Long id, @RequestParam(value = "ban", required = false, defaultValue = "true") boolean ban) {
        UserDTO user;
        if(ban){
            user = userService.banUser(id);
        }
        else{
            user = userService.unbanUser(id);
        }

        return user;
    }
    
}
