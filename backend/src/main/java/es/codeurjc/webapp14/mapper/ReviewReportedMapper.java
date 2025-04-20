package es.codeurjc.webapp14.mapper;
import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.webapp14.dto.ReviewReportedDTO;
import es.codeurjc.webapp14.model.Review;

@Mapper(componentModel = "spring")

public interface ReviewReportedMapper {

    ReviewReportedDTO toDTO(Review review);

    List<ReviewReportedDTO> toDTOs(List<Review> reviews);

    Review toDomain(ReviewReportedDTO reviewDTO);

}
