package es.codeurjc.webapp14.mapper;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.model.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDTO toDTO(Review review);

    List<ReviewDTO> toDTOs(List<Review> reviews);

    Review toDomain(ReviewDTO reviewDTO);
}
