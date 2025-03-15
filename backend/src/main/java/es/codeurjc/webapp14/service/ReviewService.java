package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.repository.ReviewRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Review> getReviewById(Long id) {
        return ReviewRepository.findById(id);
    }
    

    public void delete(Long id) {
        ReviewRepository.deleteById(id);
    }
    
}
