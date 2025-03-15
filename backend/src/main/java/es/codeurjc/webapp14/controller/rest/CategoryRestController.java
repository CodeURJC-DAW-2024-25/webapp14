package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> getCategoryData(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> data = new HashMap<>();

        // Get products by category
        data.put("products", productService.getProductsByCategory(category, page)
                .getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList());

        // Get total pages
        data.put("totalPages", productService.getProductsByCategory(category, page).getTotalPages());

        return ResponseEntity.ok(data);
    }
}
