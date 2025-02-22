package es.codeurjc.webapp14.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;


@Controller
@RequestMapping("/index")
public class ProductController { 


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean logged = (Boolean) session.getAttribute("logged");
        String userName = (String) session.getAttribute("userName");
        Long sessionUserId = (Long) session.getAttribute("userId");
        Boolean admin = session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin");

        if (logged != null && logged) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", userName);
            model.addAttribute("admin", admin);
           
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false);
        }
    }

    @GetMapping
    public String listProducts(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long sessionUserId = (Long) session.getAttribute("userId");

        if (sessionUserId != null) {
            model.addAttribute("productsRecommended", productService.getRecommendedProductsBasedOnLastOrder(sessionUserId));
        }
        
        model.addAttribute("products", productService.getAllProductsSold());
        return "user/index";
    }

    @GetMapping("/elem_detail/{id}")
    public String ProductDetails(Model model, @PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("product", productService.getProductById(id));
        List<Review> reviews = productService.getProductById(id).getTwoReviews(0, 2);
        for (Review review : reviews){
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail == null){
                review.setOwn(false);
            }
            else{
                if(userEmail.equals(review.getUser().getEmail())){
                    review.setOwn(true);
                }
                else{
                    review.setOwn(false);
                }
            }
        }
        model.addAttribute("reviews", productService.getProductById(id).getTwoReviews(0, 2));
        return "user/elem_detail";
    }

    @PostMapping("/{productId}/{reviewId}/report")
    public String reportReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);

        review.setReported(true);
        reviewService.saveReview(review);
        return "redirect:/index/elem_detail/" + productId;
    }
    
    @PostMapping("/{productId}/{reviewId}/delete")
    public String deleleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.getReviewById(reviewId);

        reviewService.delete(reviewId);
        return "redirect:/index/elem_detail/" + productId;
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


    @GetMapping("/moreReviews")
    public String getMoreReviews(
        @RequestParam Long id,
            @RequestParam int from,  
            @RequestParam int to,
            Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Product product = productService.getProductById(id);
        List<Review> reviewsList = product.getTwoReviews(from, to);
        for (Review review : reviewsList){
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail == null){
                review.setOwn(false);
            }
            else{
                if(userEmail.equals(review.getUser().getEmail())){
                    review.setOwn(true);
                }
                else{
                    review.setOwn(false);
                }
            }
        }
        boolean hasMore = to < product.getReviews().size();

        model.addAttribute("reviews", reviewsList);
        model.addAttribute("hasMore", hasMore);

        return "user/moreReviews";
    }

    
    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(@RequestParam(value = "query", required = false) String query) {
        if (query == null || query.isEmpty()) {
            return new ArrayList<>();
        }
        return productService.searchProductsByName(query);
    }

}
