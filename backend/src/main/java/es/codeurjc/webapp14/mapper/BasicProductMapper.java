package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.model.Product;

@Mapper(componentModel = "spring")
public interface BasicProductMapper {

    BasicProductDTO toDTO(Product product);

    List<BasicProductDTO> toDTOs(List<Product> products);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "sizes", ignore = true)
    Product toDomain(BasicProductDTO productDTO);
    
}
