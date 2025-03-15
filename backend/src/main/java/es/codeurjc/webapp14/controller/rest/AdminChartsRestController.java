package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.mapper.UserMapper;
import es.codeurjc.webapp14.mapper.OrderMapper;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private OrderMapper orderMapper;

        @Autowired
        private ProductMapper productMapper;

        @GetMapping
        public ResponseEntity<Map<String, Object>> getChartsData() {
                Map<String, Object> data = new HashMap<>();

                // Get all users
                List<User> users = userService.getAllUsers();
                data.put("users", users.stream()
                                .map(userMapper::toDTO)
                                .toList());

                // Get all orders
                List<Order> orders = orderService.getAllOrders();
                data.put("orders", orders.stream()
                                .map(orderMapper::toDTO)
                                .toList());

                // Get top 5 best selling products
                List<Product> topProducts = productService.getTop5BestSellingProducts();
                data.put("topProducts", topProducts.stream()
                                .map(productMapper::toDTO)
                                .toList());

                // Get products out of stock
                List<Product> outOfStockProducts = productService.getAllProductsOutOfStock();
                data.put("outOfStockProducts", outOfStockProducts.stream()
                                .map(productMapper::toDTO)
                                .toList());

                // Get total stock
                data.put("totalStock", productService.getTotalStockOfAllProducts());

                // Get products with all sizes out of stock
                data.put("productsWithAllSizesOutOfStock", productService.countProductsWithAllSizesOutOfStock());

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
