package es.codeurjc.webapp14.dto;


public record NewProductRequestDTO (
    String name,
    String description,
    Double price,
    Integer stock,
    String category,
    Boolean imageBool
) {}
