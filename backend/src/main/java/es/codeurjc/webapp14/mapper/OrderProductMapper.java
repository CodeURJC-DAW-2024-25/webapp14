package es.codeurjc.webapp14.mapper;

import org.springframework.stereotype.Component;
import es.codeurjc.webapp14.dto.OrderProductDTO;
import es.codeurjc.webapp14.model.OrderProduct;

import java.math.BigDecimal;

@Component
public class OrderProductMapper {

    public OrderProductDTO toDTO(OrderProduct orderProduct) {
        if (orderProduct == null) {
            return null;
        }

        OrderProductDTO dto = new OrderProductDTO();
        dto.setId(orderProduct.getId());
        dto.setProductId(orderProduct.getProduct().getId());
        dto.setProductName(orderProduct.getProduct().getName());
        dto.setSize(orderProduct.getSize());
        dto.setQuantity(orderProduct.getQuantity());
        dto.setPrice(BigDecimal.valueOf(orderProduct.getSubtotalPrice()));

        return dto;
    }
}
