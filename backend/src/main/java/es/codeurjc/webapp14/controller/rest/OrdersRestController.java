package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Admin_Orders", description = "Endpoints for managing Orders as an admin")
public class OrdersRestController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Get Orders", description = "Return all the Orders created")
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
