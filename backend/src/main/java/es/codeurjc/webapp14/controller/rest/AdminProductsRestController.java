package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.ProductCreateRequestDTO;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductsRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated products
        data.put("products", productService.getProductsPaginated(page, size)
                .getContent()
                .stream()
                .map(productMapper::toDTO)
                .toList());

        // Get total pages
        data.put("totalPages", productService.getProductsPaginated(page, size).getTotalPages());

        // Get total products
        data.put("totalProducts", productService.getTotalProducts());

        // Get total stock
        data.put("totalStock", productService.getTotalStock());

        // Get out of stock products count
        data.put("outOfStockProducts", productService.getOutOfStockProductsCount());

        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
        Product product = productMapper.toEntity(productCreateRequestDTO);
        Product createdProduct = productService.saveProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productMapper.toDTO(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
            @RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
        Product existingProduct = productService.getProductById(id).orElse(null);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        Product updatedProduct = productMapper.toEntity(productCreateRequestDTO);
        updatedProduct.setId(id); // Mantener el ID del producto actual
        Product savedProduct = productService.saveProduct(updatedProduct);

        return ResponseEntity.ok(productMapper.toDTO(savedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product existingProduct = productService.getProductById(id).orElse(null);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
