package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.mapper.ReviewMapper;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repository.ProductRepository;
import es.codeurjc.webapp14.repository.ReviewRepository;
import es.codeurjc.webapp14.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Service
public class ReviewService {

     @Autowired
    private ReviewMapper reviewMapper;

    private final ReviewRepository ReviewRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository ReviewRepository, UserService userService, ProductRepository ProductRepository, UserRepository userRepository) {
        this.ReviewRepository = ReviewRepository;
        this.userService = userService;
        this.productRepository = ProductRepository;
        this.userRepository = userRepository;
    }

    public List<ReviewDTO> getAllReviews() {
        return toDTOs(ReviewRepository.findAll());
    }

    public Review saveReview(Review Review) {
        return ReviewRepository.save(Review);
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = ReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        return toDTO(review);
        
    }

    public Review getReviewByIdRest(Long reviewId) {
        Review review = ReviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        return review;
    }

    public Boolean getReviewProduct(Long reviewId, Long productId) {
        Review review = getReviewByIdRest(reviewId);
        System.out.println("PRODUCT ID:" + productId);
        System.out.println("2. PRODUCT ID: " + review.getProduct().getId());
        return !review.getProduct().getId().equals(productId);
    }
    

    public void delete(Long id) {
        ReviewRepository.deleteById(id);
    }
    

    public boolean isUserBanned(Long userId) {
        UserDTO user = userService.findById(userId);
        return user.banned();
    }

    public void processReviews(Long id, Long userId) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<Review> reviews = product.getReviews();

        UserDTO user = userService.findById(userId);

        String userEmail = user.email();

        for (Review review : reviews) {
            review.setOwn(userEmail != null && userEmail.equals(review.getUser().getEmail()));
            review.updateStars();

            review.setRating1(review.getRating() == 1);
            review.setRating2(review.getRating() == 2);
            review.setRating3(review.getRating() == 3);
            review.setRating4(review.getRating() == 4);
            review.setRating5(review.getRating() == 5);

            ReviewRepository.save(review);
        }

    }

    public ReviewDTO createOrReplaceReview(Long id, ReviewDTO reviewDTO, Long productId, Long userId) {
		ReviewDTO review;
		if(id == null) {
			review = createReview(reviewDTO, productId, userId);
		} else {
			review = replaceReview(id, reviewDTO, productId, userId);
		}
		return review;
	}

    private ReviewDTO createReview(ReviewDTO reviewDTO, Long productId, Long userId) {
        if(reviewDTO.id() != null) {
			throw new IllegalArgumentException();
		}

		Review review = toDomain(reviewDTO);


        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        review.setProduct(product);

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        review.setUser(user);

        review.updateStars();

		ReviewRepository.save(review);

		return toDTO(review);
    }

    private ReviewDTO replaceReview(Long id, ReviewDTO updatedReviewDTO, Long productId, Long userId) {

        Review oldReview = ReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        if(!oldReview.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have permission to modify this review");
        }

        Review updatedReview = toDomain(updatedReviewDTO);
		updatedReview.setId(id);
        
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        updatedReview.setProduct(product);

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        updatedReview.setUser(user);

	    ReviewRepository.save(updatedReview);

		return toDTO(updatedReview);
    }


    private ReviewDTO toDTO(Review review) {
		return reviewMapper.toDTO(review);
	}

    private Review toDomain(ReviewDTO reviewDTO) {
		return reviewMapper.toDomain(reviewDTO);
	}

	public List<ReviewDTO> toDTOs(List<Review> reviews) {
		return reviewMapper.toDTOs(reviews);
	}

    public List<ReviewDTO> getReviewsByProductId(Long id) {
        List<Review> reviews = ReviewRepository.findByProductId(id);
        return toDTOs(reviews);
    }

    public ReviewDTO reportReview(Long reviewId) {

        Review review = ReviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setReported(true);
        //this.user.setReports(1);

        saveReview(review);

        return toDTO(review);
    }

    public ReviewDTO acceptReview(Long id) {

        Review review = ReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setReported(false);
        saveReview(review);

        return toDTO(review);
    }

    public ReviewDTO deleteReview(Long id, Long userId) {

        Review oldReview = ReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        if(!oldReview.getUser().getId().equals(userId) && !userService.findUserById(userId).getRoles().contains("ADMIN")){
            throw new AccessDeniedException("You do not have permission to delete this review");
        }

        Review review = ReviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));

        ReviewRepository.delete(review);

        return toDTO(review);

    }

    public Page<ReviewDTO> getTwoReviews(Product product, Pageable pageable) {
    Page<Review> reviewsPage = ReviewRepository.findByProduct(product, pageable);
    
    List<ReviewDTO> dtoList = toDTOs(reviewsPage.getContent());

    return new PageImpl<>(dtoList, pageable, reviewsPage.getTotalElements());
}

    
}
