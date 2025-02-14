/*package es.codeurjc.webapp14.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.webapp14.model.ProductEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;


@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void init(){
        ProductEntity p1 = new ProductEntity();
        productRepository.save(p1);

    }

    @GetMapping("/")
    public String getProducts(Model model) {
        model.addAttribute("product", productRepository.findAll());
        return "index";
    }
    

}

*/