package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(target = "userId", source = "user.id") // Solo incluimos el ID del usuario
    OrderDTO toDTO(Order order);

    Order toDomain(OrderDTO orderDTO);

    List<OrderDTO> toDTOs(List<Order> orders);

}
