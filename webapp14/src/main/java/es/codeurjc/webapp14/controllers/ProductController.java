package es.codeurjc.webapp14.controllers;

import java.sql.SQLException;
import java.sql.Blob;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.services.ProductService;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/index")
public class ProductController { 

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProductsSold());
        return "user/index";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        Blob imageBlob = product.getImage();
        if (imageBlob != null) {
            try {
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok().headers(headers).body(imageBytes);
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/elem_detail/{id}")
    public String ProductDetails(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("reviews", productService.getProductById(id).getReviews());
        return "user/elem_detail";
    }

    @GetMapping("/category/{category}")
    public String listProductsByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productsPage = productService.getProductsByCategory(category, page);

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("category", category);
        return "user/category";
    }

    @GetMapping("/moreProducts") 
    public String getMoreProducts(
            @RequestParam String category,
            @RequestParam int page,
            Model model) { 

        Page<Product> productsPage = productService.getProductsByCategory(category, page);
        boolean hasMore = page < productsPage.getTotalPages() - 1;

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "user/moreProducts";
    }



}
