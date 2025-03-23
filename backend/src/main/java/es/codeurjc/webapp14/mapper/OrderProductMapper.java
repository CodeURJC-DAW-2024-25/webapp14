package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import es.codeurjc.webapp14.dto.OrderProductDTO;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    OrderProductDTO toDTO(OrderProduct order);

    Order toDomain(OrderProductDTO orderProductDTO);

    List<OrderProductDTO> toDTOs(List<OrderProduct> orders);


}
