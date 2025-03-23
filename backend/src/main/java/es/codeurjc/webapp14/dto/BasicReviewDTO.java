package es.codeurjc.webapp14.dto;

public record BasicReviewDTO(
    Long id,
    String username,
    int rating,
    String reviewText,
    boolean reported,
    boolean own
) {}
