package es.codeurjc.webapp14.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/v1/reviews")
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

    @GetMapping("/all")
    public List<ReviewDTO> getReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ReviewDTO getReview(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }


    @PostMapping("/{productId}")
    public ResponseEntity<ReviewDTO> createReview(@ModelAttribute("userId") long userId, Model model, @RequestBody NewReviewRequestDTO newReviewRequestDTO, @PathVariable Long productId) throws IOException, SQLException {

        ReviewDTO createdReview = createOrReplaceReview(newReviewRequestDTO, null, userId, productId);
        URI location = URI.create("https://localhost:8443/api/v1/reviews/" + createdReview.id());

		return ResponseEntity.created(location).body(createdReview);

    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> reportReview(@ModelAttribute("userId") long userId, 
                                                @PathVariable Long reviewId) {
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

    @PutMapping("/{reviewId}/{productId}")
    public ReviewDTO replaceReview(@ModelAttribute("userId") long userId, Model model, @RequestBody NewReviewRequestDTO newReviewRequestDTO, @PathVariable Long reviewId, @PathVariable Long productId) throws IOException, SQLException {

        return createOrReplaceReview(newReviewRequestDTO, reviewId, userId, productId);
  
    }

    @DeleteMapping("/{reviewId}/{productId}")
    public ReviewDTO deleteReview(@ModelAttribute("userId") long userId, Model model, @PathVariable Long reviewId, @PathVariable Long productId) throws IOException, SQLException {
        
        return reviewService.deleteReview(reviewId, userId);

    }
}
