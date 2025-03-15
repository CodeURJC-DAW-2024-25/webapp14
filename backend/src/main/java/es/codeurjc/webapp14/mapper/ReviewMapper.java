package es.codeurjc.webapp14.mapper;

import org.springframework.stereotype.Component;
import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.User;

@Component
public class ReviewMapper {

    public ReviewDTO toDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUsername());
        dto.setProductId(review.getProduct().getId());
        dto.setRating(review.getRating());
        dto.setReviewText(review.getReviewText());
        dto.setReported(review.isReported());
        dto.setOwn(review.getOwn());

        return dto;
    }

    public Review toEntity(ReviewDTO dto, Product product, User user) {
        if (dto == null) {
            return null;
        }

        Review review = new Review();
        review.setId(dto.getId());
        review.setRating(dto.getRating());
        review.setReviewText(dto.getReviewText());
        review.setReported(dto.isReported());
        review.setProduct(product);
        review.setUser(user);

        return review;
    }
}
