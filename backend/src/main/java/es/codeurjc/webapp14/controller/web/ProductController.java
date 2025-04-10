package es.codeurjc.webapp14.controller.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
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

import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.dto.NewReviewRequestDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.ReviewService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.User;
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
        List<BasicProductDTO> products = productService.searchProductsByName(query);
        model.addAttribute("productsSearch", products);
        model.addAttribute("query", false);
        model.addAttribute("open", false);
    }

    @GetMapping
    public String listProducts(Model model, @ModelAttribute("userId") Long userId) {

        if (userId != null) {

            userService.findById(userId);
            model.addAttribute("productsRecommended",
                    productService.getRecommendedProductsBasedOnLastOrder(userId, 0, 2));
        }

        model.addAttribute("products", productService.getAllProductsSold());
        return "user/index";
    }

    @GetMapping("/elem_detail/{id}")
    public String ProductDetails(@RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "2") int size,
    Model model, @PathVariable Long id, @ModelAttribute("userId") Long userId) {


        Optional<Product> productWeb = productService.getProductByIdWeb(id);
        if(!productWeb.isPresent()){
            return "redirect:/no-page-error";
        }
        ProductDTO product = productService.getProductById(id);

        Page<ReviewDTO> reviewsPage = productService.getTwoReviews(page, size, product);
        List<ReviewDTO> reviews = reviewsPage.getContent();


        if (userId != null) {
            boolean isBanned = reviewService.isUserBanned(userId);
            model.addAttribute("banned", isBanned);
        
            if (isBanned) {
                return "redirect:/no-page-error";
            }
            reviewService.processReviews(id, userId);

            for (ReviewDTO review : reviews) {
                model.addAttribute("rating" + review.rating(), true);
                //model.addAttribute("rating5", true);
                    int rating = review.rating();
                    model.addAttribute("ratingStars", Collections.nCopies(rating, true));
                    model.addAttribute("emptyStars" , Collections.nCopies(5 - rating, true));
                
            }

        } else {
            model.addAttribute("banned", false);
        }

        reviewsPage = productService.getTwoReviews(page, size, product);
        reviews = reviewsPage.getContent();

        model.addAttribute("reviews", reviews);
        model.addAttribute("hasMore", reviewsPage.hasNext());
        model.addAttribute("nextPage", page + 1);
        
        model.addAttribute("hasSizeS", productService.getSize(id,"S"));
        model.addAttribute("hasSizeM", productService.getSize(id,"M"));
        model.addAttribute("hasSizeL", productService.getSize(id,"L"));
        model.addAttribute("hasSizeXL", productService.getSize(id,"XL"));
        
        model.addAttribute("product", product);
        model.addAttribute("productId", product.id());

        return "user/elem_detail";
    }

    @PostMapping("/{productId}/{reviewId}/report")
    public String reportReview(@PathVariable Long productId, @PathVariable Long reviewId) {

        reviewService.reportReview(reviewId);

        return "redirect:/index/elem_detail/" + productId;
    }

    @PostMapping("/{productId}/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId, @ModelAttribute("userId") long userId ,@PathVariable Long reviewId, Model model) {

        reviewService.deleteReview(reviewId, userId);
        return "redirect:/index/elem_detail/" + productId;
    }

    @PostMapping("/{userId}/{productId}/{reviewId}/edit")
    public String editReview(@PathVariable Long productId,
            NewReviewRequestDTO newReviewRequestDTO,
            @PathVariable Long userId, @PathVariable Long reviewId) throws IOException, SQLException {
        
        createOrReplaceReview(newReviewRequestDTO, reviewId, userId, productId);

        return "redirect:/index/elem_detail/" + productId;
    }

    @PostMapping("/{userId}/{productId}/addReview")
    public String addReview(NewReviewRequestDTO newReviewRequestDTO, @PathVariable Long userId, @PathVariable Long productId) throws IOException, SQLException {

        createOrReplaceReview(newReviewRequestDTO, null, userId, productId);

        return "redirect:/index/elem_detail/" + productId;
    }


    private ReviewDTO createOrReplaceReview(NewReviewRequestDTO newReviewRequestDTO, Long reviewId, Long userId, Long productId)
			throws SQLException, IOException {
	
		ReviewDTO reviewDTO = new ReviewDTO(reviewId, userService.findById(userId).name(), userService.findById(userId),
            newReviewRequestDTO.rating(),
            newReviewRequestDTO.reviewText(), false, true, Collections.nCopies(0, true), Collections.nCopies(5, true));
				
	    ReviewDTO newReviewDTO = reviewService.createOrReplaceReview(reviewId, reviewDTO, productId, userId);

		return newReviewDTO;
	}

    @GetMapping("/category/{category}")
    public String listProductsByCategory(@PathVariable String category, @RequestParam(defaultValue = "0") int page,
            Model model) {

        List<String> validCategories = List.of("camisetas", "pantalones", "abrigos", "jerseys");

        if (!validCategories.contains(category.toLowerCase())) {
            return "redirect:/no-page-error";
        }

        Page<BasicProductDTO> productsPage = productService.getProductsByCategory(category, page);

        model.addAttribute("products", productsPage);
        model.addAttribute("category", category);
        return "user/category";
    }

    @GetMapping("/moreProducts")
    public String getMoreProducts(
            @RequestParam String category,
            @RequestParam int page,
            Model model) {

        Page<BasicProductDTO> productsPage = productService.getProductsByCategory(category, page);
        boolean hasMore = page < productsPage.getTotalPages() - 1;

        model.addAttribute("products", productsPage);
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

        Page<BasicProductDTO> productsPage = productService.getRecommendedProductsBasedOnLastOrder(userId, page, size);
        boolean hasMore = page < productsPage.getTotalPages() - 1;

        model.addAttribute("productsRecommended", productsPage);
        model.addAttribute("hasMore", hasMore);

        return "user/moreRecProducts";
    }

    @GetMapping("/moreReviews")
    public String getMoreReviews(
            @RequestParam Long id,
            @RequestParam int page,
            @RequestParam int size,
            Model model, @ModelAttribute("userId") Long userId) {
        ProductDTO product = productService.getProductById(id);

        Page<ReviewDTO> reviewsPage = productService.getTwoReviews(page, size, product);
        List<ReviewDTO> reviews = reviewsPage.getContent();  
       
        if (userId != null) {
            boolean isBanned = reviewService.isUserBanned(userId);
            model.addAttribute("banned", isBanned);
        
            if (isBanned) {
                return "redirect:/no-page-error";
            }
            reviewService.processReviews(id, userId);

            for (ReviewDTO review : reviews) {
                model.addAttribute("rating" + review.rating(), true);
                //model.addAttribute("rating5", true);
            }

        } else {
            model.addAttribute("banned", false);
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("hasMore", reviewsPage.hasNext());
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("productId", product.id());

        return "user/moreReviews";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "query", required = false) String query, Model model) {

        if (query != null && !query.isEmpty()) {
            List<BasicProductDTO> products = productService.searchProductsByName(query);
            model.addAttribute("productsSearch", products);
            model.addAttribute("query", query);
            model.addAttribute("open", true);
        }

        return "user/index";
    }

}
