package es.codeurjc.webapp14.dto;

import java.util.List;

public record ReviewDTO(
    Long id,
    String username,
    UserDTO user,
    int rating,
    String reviewText,
    boolean reported,
    boolean own,
    List<Boolean> ratingStars,
    List<Boolean> emptyStars
) { }
