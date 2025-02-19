package es.codeurjc.webapp14.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.services.ProductService;

@Controller
@RequestMapping("/image")
public class ImageController {
    private final ProductService productService;

    public ImageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok().headers(headers).body(product.getImage());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
