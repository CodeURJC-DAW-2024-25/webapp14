package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository ReviewRepository;

    public ReviewService(ReviewRepository ReviewRepository) {
        this.ReviewRepository = ReviewRepository;
    }

    public List<Review> getAllReviews() {
        return ReviewRepository.findAll();
    }

    public Review saveReview(Review Review) {
        return ReviewRepository.save(Review);
    }

    public Review getReviewById(Long id) {
        return ReviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reviewo no encontrado con ID: " + id));
    }

    public void delete(Long id) {
        ReviewRepository.deleteById(id);
    }
    
}
