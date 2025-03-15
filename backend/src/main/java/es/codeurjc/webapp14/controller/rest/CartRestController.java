package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCartData(@RequestParam Long userId) {
        Map<String, Object> data = new HashMap<>();

        // Get user's unpaid order (cart)
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user.get());
        if (unpaidOrder.isPresent()) {
            data.put("cart", orderMapper.toDTO(unpaidOrder.get()));
        }

        return ResponseEntity.ok(data);
    }
}
