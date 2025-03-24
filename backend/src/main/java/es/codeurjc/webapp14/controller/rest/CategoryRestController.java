package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@Tag(name = "Category", description = "Endpoints for managing Products of a category")
public class CategoryRestController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get category Products", description = "Return the products that belongs to the category")
    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> getCategoryData(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> data = new HashMap<>();

        List<String> validCategories = List.of("camisetas", "pantalones", "abrigos", "jerseys");

        if (!validCategories.contains(category.toLowerCase())) {
            return null;
        }

        // Get products by category
        data.put("products", productService.getProductsByCategory(category, page));

        // Get category name
        data.put("category", category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase());

        return ResponseEntity.ok(data);
    }
}
