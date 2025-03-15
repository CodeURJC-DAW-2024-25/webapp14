package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.mapper.OrderMapper;
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

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated orders
        data.put("orders", orderService.getOrdersPaginated(page, size)
                .getContent()
                .stream()
                .map(orderMapper::toDTO)
                .toList());

        // Get total pages
        data.put("totalPages", orderService.getOrdersPaginated(page, size).getTotalPages());

        // Get total sales
        data.put("totalSales", orderService.getTotalSales());

        // Get today's sales
        data.put("todaySales", orderService.getTodaySales());

        // Get total orders
        data.put("totalOrders", orderService.getTotalOrders());

        // Get orders last 30 days
        data.put("ordersLast30Days", orderService.getOrdersLast30Days());

        return ResponseEntity.ok(data);
    }
}
