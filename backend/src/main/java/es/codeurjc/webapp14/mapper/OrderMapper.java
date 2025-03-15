package es.codeurjc.webapp14.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.dto.OrderProductDTO;
import es.codeurjc.webapp14.model.Order;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderProductMapper orderProductMapper;

    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setDate(order.getCreatedAt());
        dto.setTotal(order.getTotalPrice());
        dto.setPaid(order.getIsPaid());
        dto.setState(order.getState());

        List<OrderProductDTO> orderProductDTOs = order.getOrderProducts().stream()
                .map(orderProductMapper::toDTO)
                .collect(Collectors.toList());
        dto.setOrderProducts(orderProductDTOs);

        return dto;
    }
}
