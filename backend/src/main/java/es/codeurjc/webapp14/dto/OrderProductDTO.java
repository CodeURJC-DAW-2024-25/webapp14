package es.codeurjc.webapp14.dto;

public record OrderProductDTO(
    Long id,
    BasicProductSizeDTO product,
    String productName,
    int quantity,
    String size,
    Double subtotalPrice
) { }
