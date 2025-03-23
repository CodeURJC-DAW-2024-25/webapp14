package es.codeurjc.webapp14.dto;

public record BasicProductDTO(
    Long id,
    String name,
    String description,
    Double price,
    Integer stock,
    boolean outOfStock,
    int sold,
    String category,
    boolean imageBool,
    String imageUrl
) {}
