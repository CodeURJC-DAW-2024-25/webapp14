package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductDetailRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {
        Map<String, Object> data = new HashMap<>();

        // Get product details
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Get product with sizes and reviews
        data.put("product", productMapper.toDTO(product.get(), true, true));

        // Get recommended products if user is logged in
        if (userId != null) {
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                data.put("recommendedProducts", productService.getRecommendedProductsBasedOnLastOrder(userId, 0, 4)
                        .getContent()
                        .stream()
                        .map(productMapper::toDTO)
                        .toList());
            }
        }

        return ResponseEntity.ok(data);
    }
}
