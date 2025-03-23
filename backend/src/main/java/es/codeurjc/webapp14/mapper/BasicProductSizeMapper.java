package es.codeurjc.webapp14.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.webapp14.dto.BasicProductSizeDTO;
import es.codeurjc.webapp14.model.Product;


@Mapper(componentModel = "spring")
public interface BasicProductSizeMapper {

    BasicProductSizeDTO toDTO(Product product);

    List<BasicProductSizeDTO> toDTOs(List<Product> products);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    Product toDomain(BasicProductSizeDTO productDTO);

}
