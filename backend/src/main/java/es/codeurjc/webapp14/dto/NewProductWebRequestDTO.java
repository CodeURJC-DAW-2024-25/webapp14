package es.codeurjc.webapp14.dto;

import org.springframework.web.multipart.MultipartFile;

public record NewProductWebRequestDTO(
    String name,
    String description,
    Double price,
    Integer stock,
    String category,
    MultipartFile imageField
) {}