package es.codeurjc.webapp14.controllers;


import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.Size.SizeName;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.UserService;
import es.codeurjc.webapp14.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;



@Controller
@RequestMapping("/index")
public class ProductController { 


    @Autowired
    private ReviewService reviewService;


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        boolean isLogged = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);
        
        model.addAttribute("logged", isLogged);

        if (isLogged) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername()); 
            
            model.addAttribute("userName", user.getName()); 
            model.addAttribute("userId", user.getId());
            model.addAttribute("admin", user.getRoles().contains("ADMIN"));
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("userId", null);
            model.addAttribute("admin", false);
        }
        
        String query = "";
        List<Product> products = productService.searchProductsByName(query);
        model.addAttribute("productsSearch", products);
        model.addAttribute("query", false);
        model.addAttribute("open", false);
    }


    @GetMapping
    public String listProducts(Model model, @ModelAttribute("userId") Long userId) {
        if (userId != null) {
            userService.findById(userId);
            model.addAttribute("productsRecommended", productService.getRecommendedProductsBasedOnLastOrder(userId, 0, 2));
        }

        model.addAttribute("products", productService.getAllProductsSold());
        return "user/index";
    }


    @GetMapping("/elem_detail/{id}")
    public String ProductDetails(Model model, @PathVariable Long id, @ModelAttribute("userId") Long userId) {
 

        Optional <Product> existproduct = productService.getProductById(id);

        if(!existproduct.isPresent()){
            return "redirect:/no-page-error";
        }

        Product product = existproduct.get();

        List<Review> reviews = product.getTwoReviews(0, 2);

        if(userId != null){

            String userEmail = userService.findById(userId).getEmail();
            System.out.println("EMAIL: " + userEmail);
        
                for (Review review : reviews) {
                    if (userEmail == null){
                        review.setOwn(false);
                    } else {
                        review.setOwn(userEmail.equals(review.getUser().getEmail()));
                    }
                    review.updateStars();
            
                    model.addAttribute("rating" + review.getRating(), true);
                    review.setRating1(review.getRating() == 1);
                    review.setRating2(review.getRating() == 2);
                    review.setRating3(review.getRating() == 3);
                    review.setRating4(review.getRating() == 4);
                    review.setRating5(review.getRating() == 5);
                }
        }

        boolean hasSizeS = false;
        boolean hasSizeM = false;
        boolean hasSizeL = false;
        boolean hasSizeXL = false;

        // Verify stock on each size
        for (Size size : product.getSizes()) {
            if (size.getName() == SizeName.S && size.getStock() > 0) hasSizeS = true;
            if (size.getName() == SizeName.M && size.getStock() > 0) hasSizeM = true;
            if (size.getName() == SizeName.L && size.getStock() > 0) hasSizeL = true;
            if (size.getName() == SizeName.XL && size.getStock() > 0) hasSizeXL = true;
        }

        model.addAttribute("hasSizeS", hasSizeS);
        model.addAttribute("hasSizeM", hasSizeM);
        model.addAttribute("hasSizeL", hasSizeL);
        model.addAttribute("hasSizeXL", hasSizeXL);
    
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        
        return "user/elem_detail";
    }
    


    @PostMapping("/{productId}/{reviewId}/report")
    public String reportReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        Optional <Review> existreview = reviewService.getReviewById(reviewId);;

        if(!existreview.isPresent()){
            return "redirect:/no-page-error";
        }

        Review review = existreview.get();

        review.setReported(true);
        reviewService.saveReview(review);
        return "redirect:/index/elem_detail/" + productId;
    }
    
    @PostMapping("/{productId}/{reviewId}/delete")
    public String deleleteReview(@PathVariable Long productId, @PathVariable Long reviewId, @ModelAttribute("userId") Long userId) {
        Optional <Review> review = reviewService.getReviewById(reviewId);
        Optional <Product> product = productService.getProductById(productId);

        if(!review.isPresent() || !product.isPresent() || !review.get().getUser().getId().equals(userId)){
            return "no_page_error";
        }


        reviewService.delete(reviewId);
        return "redirect:/index/elem_detail/" + productId;
    }
    

    @PostMapping("/{productId}/{reviewId}/edit")
    public String editReview(@PathVariable Long productId,
                             @PathVariable Long reviewId,
                             @RequestParam int rating,
                             @RequestParam String reviewText,
                             @ModelAttribute("userId") Long userId) {
    
        if (userId == null) {
            return "redirect:/login"; 
        }

        User user = userService.findById(userId);
        String userEmail = user.getEmail();
    
        Optional <Review> existreview = reviewService.getReviewById(reviewId);;

        if(!existreview.isPresent()){
            return "redirect:/no-page-error";
        }

        Review review = existreview.get();
    
        if (userEmail == null || !userEmail.equals(review.getUser().getEmail())) {
            return "redirect:/access-error";
        }
    
        review.setReviewText(reviewText);
        review.setRating(rating);
        review.updateStars();
    
        reviewService.saveReview(review);
        return "redirect:/index/elem_detail/" + productId;
    }
    


    @PostMapping("/{productId}/addReview")
    public String addReview(@PathVariable Long productId,
                            @RequestParam int rating,
                            @RequestParam("review-text") String reviewText,
                            @ModelAttribute("userId") Long userId) {

        if (userId == null) {
            return "redirect:/login"; 
        }

        User user = userService.findById(userId);
        Optional <Product> existproduct = productService.getProductById(productId);

        if(!existproduct.isPresent()){
            return "redirect:/no-page-error";
        }

        Product product = existproduct.get();

        Review newReview = new Review(rating, reviewText, false, product, user);
        newReview.updateStars(); 

        reviewService.saveReview(newReview);

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

    @GetMapping("/moreRecProducts") 
    public String getMoreRecProducts(
            @RequestParam int page,
            @RequestParam int size,
            Model model,
            @ModelAttribute("userId") Long userId) { 
        
        if (userId == null) {
            return "redirect:/login"; 
        }

        Page<Product> productsPage = productService.getRecommendedProductsBasedOnLastOrder(userId, page, size);
        boolean hasMore = page < productsPage.getTotalPages() - 1;

        model.addAttribute("productsRecommended", productsPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "user/moreRecProducts";
    }


    @GetMapping("/moreReviews")
    public String getMoreReviews(
        @RequestParam Long id,
            @RequestParam int from,  
            @RequestParam int to,
            Model model, @ModelAttribute("userId") Long userId) {
        Optional <Product> existproduct = productService.getProductById(id);

        if(!existproduct.isPresent()){
            return "redirect:/no-page-error";
        }

        Product product = existproduct.get();
        List<Review> reviewsList = product.getTwoReviews(from, to);
        for (Review review : reviewsList){
            if (userId == null){
                review.setOwn(false);
            }
            else{
                User user = userService.findById(userId);
                if(user.getEmail().equals(review.getUser().getEmail())){
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
    public String searchProducts(@RequestParam(value = "query", required = false) String query, Model model) {

        System.out.println("Busco");

        if (query != null && !query.isEmpty()) {
            List<Product> products = productService.searchProductsByName(query);
            model.addAttribute("productsSearch", products);
            model.addAttribute("query", query);
            model.addAttribute("open", true);
        }
        return "user/index";
    }


}
