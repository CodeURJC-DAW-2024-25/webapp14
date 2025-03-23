package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrdersRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get total orders
        data.put("totalOrders", orderService.getTotalOrders());

        // Get paginated orders
        // Refactor? products paginated has hasMore and nextPage attributes?
        data.put("orders", orderService.getOrdersPaginated(page, size));

        return ResponseEntity.ok(data);
    }
}
