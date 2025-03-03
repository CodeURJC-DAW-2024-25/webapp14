
package es.codeurjc.webapp14.controllers;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        Optional<Product> existproduct = productService.getProductById(id);

        Product product = existproduct.get();

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        if (product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Blob imageBlob = product.getImage();
            byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return ResponseEntity.ok().headers(headers).body(imageBytes);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
