package es.codeurjc.webapp14.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.services.ProductService;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/elem_detail/{id}")
    public String ProductDetails(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("reviews", productService.getProductById(id).getReviews());
        return "user/elem_detail";
    }

    @GetMapping("/category/{category}")
    public String listProductsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        return "user/category";
    }

}
