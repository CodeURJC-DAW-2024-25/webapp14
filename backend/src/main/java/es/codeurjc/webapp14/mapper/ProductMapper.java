package es.codeurjc.webapp14.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

import es.codeurjc.webapp14.dto.ProductCreateRequestDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    //@Mapping(target = "orderProducts", ignore = true)
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDTOs(List<Product> products);

    @Mapping(target = "image", ignore = true)
    Product toDomain(ProductDTO productDTO);
    
    @Mapping(target = "id", ignore = true) // Ignoramos el ID porque se genera automáticamente
    Product toEntity(ProductCreateRequestDTO dto);
}
