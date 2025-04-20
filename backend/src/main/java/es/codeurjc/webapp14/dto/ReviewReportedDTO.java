package es.codeurjc.webapp14.dto;

import java.util.List;

public record ReviewReportedDTO(
    Long id,
    String username,
    ProductDTO product,
    int rating,
    String reviewText,
    boolean reported,
    boolean own,
    List<Boolean> ratingStars,
    List<Boolean> emptyStars
) {}