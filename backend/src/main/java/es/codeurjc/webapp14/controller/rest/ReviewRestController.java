package es.codeurjc.webapp14.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.webapp14.dto.NewReviewRequestDTO;
import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.ReviewService;
import es.codeurjc.webapp14.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Reviews", description = "Endpoints for managing reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;


    @Autowired
    private UserService userService;

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
            model.addAttribute("userId", 0);
            model.addAttribute("admin", false);
        }
    }

    /*@Operation(summary = "Get all the Reviews", description = "Return all the Reviews created")
    @GetMapping("/all")
    public List<ReviewDTO> getReviews() {
        return reviewService.getAllReviews();
    }
    */

    @Operation(summary = "Get Reviews", description = "Return a single review")
    @GetMapping("/{productId}/reviews/{reviewId}")
    public ReviewDTO getReview(@PathVariable Long reviewId, @PathVariable Long productId) {
        if(reviewService.getReviewProduct(reviewId,productId)){
            throw new EntityNotFoundException("Review not found");
        }
        return reviewService.getReviewById(reviewId);
    }


    @Operation(summary = "Create Review", description = "Create and save a Review on the databse")
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDTO> createReview(@ModelAttribute("userId") long userId, Model model, @RequestBody NewReviewRequestDTO newReviewRequestDTO, @PathVariable Long productId) throws IOException, SQLException {

        ReviewDTO createdReview = createOrReplaceReview(newReviewRequestDTO, null, userId, productId);
        URI location = URI.create("https://localhost:8443/api/v1/reviews/" + createdReview.id());

		return ResponseEntity.created(location).body(createdReview);

    }

    @Operation(summary = "Report Review", description = "Report a Review from a User")
    @PatchMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> reportReview(@ModelAttribute("userId") long userId, 
                                                @PathVariable Long reviewId, @PathVariable Long productId) {

        if(reviewService.getReviewProduct(reviewId,productId)){
            throw new EntityNotFoundException("Review not found");
        }
        ReviewDTO reportedReview = reviewService.reportReview(reviewId);
        return ResponseEntity.ok(reportedReview);

    }


    private ReviewDTO createOrReplaceReview(NewReviewRequestDTO newReviewRequestDTO, Long reviewId, Long userId, Long productId)
			throws SQLException, IOException {

		ReviewDTO reviewDTO = new ReviewDTO(reviewId, userService.findById(userId).name(), userService.findById(userId),
            newReviewRequestDTO.rating(),
            newReviewRequestDTO.reviewText(), false, true, Collections.nCopies(0, true), Collections.nCopies(5, true));
				
	    ReviewDTO newReviewDTO = reviewService.createOrReplaceReview(reviewId, reviewDTO, productId, userId);

		return newReviewDTO;
	}

    @Operation(summary = "Edit Review", description = "Edit a created Review")
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ReviewDTO replaceReview(@ModelAttribute("userId") long userId, Model model, @RequestBody NewReviewRequestDTO newReviewRequestDTO, @PathVariable Long reviewId, @PathVariable Long productId) throws IOException, SQLException {

        if(reviewService.getReviewProduct(reviewId,productId)){
            throw new EntityNotFoundException("Review not found");
        }
        return createOrReplaceReview(newReviewRequestDTO, reviewId, userId, productId);
  
    }

    @Operation(summary = "Delete Review", description = "Delete a created Review")
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ReviewDTO deleteReview(@ModelAttribute("userId") long userId, Model model, @PathVariable Long reviewId, @PathVariable Long productId) throws IOException, SQLException {
        
        if(reviewService.getReviewProduct(reviewId,productId)){
            throw new EntityNotFoundException("Review not found");
        }
        return reviewService.deleteReview(reviewId, userId);

    }
}
