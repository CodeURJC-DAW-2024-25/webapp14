
package es.codeurjc.webapp14.controller.web;

import java.sql.SQLException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.webapp14.service.ProductService;
import java.io.IOException;

@Controller
@RequestMapping("/image")
public class ImageController {
    private final ProductService productService;

    public ImageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {

		Resource productImage = productService.getProductImage(id);

		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE)
				.body(productImage);

	}
}
