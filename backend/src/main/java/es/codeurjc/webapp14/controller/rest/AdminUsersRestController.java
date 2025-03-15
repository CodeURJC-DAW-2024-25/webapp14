package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated users
        data.put("users", userService.getUsersPaginated(page, size)
                .getContent()
                .stream()
                .map(userMapper::toDTO)
                .toList());

        // Get total pages
        data.put("totalPages", userService.getUsersPaginated(page, size).getTotalPages());

        // Get total users
        data.put("totalUsers", userService.getTotalUsers());

        // Get admin users count
        data.put("adminUsers", userService.getAdminUsersCount());

        // Get regular users count
        data.put("regularUsers", userService.getRegularUsersCount());

        return ResponseEntity.ok(data);
    }
}
