package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/products")
public class ProductDetailRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ProductDTO getProductDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {

        return productService.getProductById(id);

    }

    
    @GetMapping("/{id}/image")
	public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {

		Resource productImage = productService.getProductImage(id);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				.body(productImage);

	}
}
