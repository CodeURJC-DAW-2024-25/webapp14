package es.codeurjc.webapp14.dto;

import java.util.List;

public record BasicProductSizeDTO(
    Long id,
    String name,
    String description,
    Double price,
    Integer stock,
    boolean outOfStock,
    int sold,
    String category,
    boolean imageBool,
    String imageUrl,
    List<SizeDTO> sizes
) {}
