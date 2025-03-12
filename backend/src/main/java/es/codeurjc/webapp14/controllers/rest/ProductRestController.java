package es.codeurjc.webapp14.controllers.rest;

import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.ProductCreateRequestDTO;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/")
    public List<ProductDTO> getProducts() {
        List<Product> products = productService.getAllProducts();
        return products.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @PostMapping("/")
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
