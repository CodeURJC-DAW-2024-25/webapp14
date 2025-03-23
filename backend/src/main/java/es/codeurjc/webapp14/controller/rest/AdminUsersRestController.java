package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUsersRestController {

    @Autowired
    private UserService userService;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {

        UserDTO user = userService.findById(id);
        
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/{id}/banned")
    public UserDTO banUser(@PathVariable Long id) {

        UserDTO user = userService.banUser(id);

        return user;
    }

    @PatchMapping("/{id}/unbanned")
    public UserDTO unbanUser(@PathVariable Long id) {

        UserDTO user = userService.unbanUser(id);

        return user;
    }

}