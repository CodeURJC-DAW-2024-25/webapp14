package es.codeurjc.webapp14.dto;

public record NewReviewRequestDTO(
    int rating,
    String reviewText
){}
