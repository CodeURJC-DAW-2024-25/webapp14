package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/charts")
public class AdminChartsRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getChartsData() {
        Map<String, Object> data = new HashMap<>();

        // Get today's sales
        data.put("todaySales", orderService.getTodaySales());

        // Get total sales
        data.put("totalSales", orderService.getTotalSales());

        // Get total orders
        data.put("totalOrders", orderService.getTotalOrders());

        // Get all users
        data.put("totalUsers", userService.getAllUsers().size());

        // Get top 5 best selling products
        data.put("top5BestSellingProducts", productService.getTop5BestSellingProducts());

        // Get orders last 30 days
        data.put("ordersLast30Days", orderService.getOrdersLast30Days());

        return ResponseEntity.ok(data);
    }
}
