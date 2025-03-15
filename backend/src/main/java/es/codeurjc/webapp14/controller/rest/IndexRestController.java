package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/index")
public class IndexRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getIndexData(@RequestParam(required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();

        // Get best selling products
        List<Product> bestSellingProducts = productService.getAllProductsSold();
        data.put("bestSellingProducts", bestSellingProducts.stream()
                .map(productMapper::toDTO)
                .toList());

        // Get recommended products if user is logged in
        if (userId != null) {
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                data.put("recommendedProducts", productService.getRecommendedProductsBasedOnLastOrder(userId, 0, 2)
                        .getContent()
                        .stream()
                        .map(productMapper::toDTO)
                        .toList());
            }
        }

        return ResponseEntity.ok(data);
    }
}
