package es.codeurjc.webapp14.dto;
import java.math.BigDecimal;
import java.util.List;

public record OrderDTO(
    Long id,
    Long userId,
    String createdAtFormatted,
    boolean isPaid,
    BigDecimal totalPrice,
    String state,
    List<OrderProductDTO> orderProducts
) {}

