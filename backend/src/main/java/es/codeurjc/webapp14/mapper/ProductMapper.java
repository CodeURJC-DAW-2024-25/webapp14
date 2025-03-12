package es.codeurjc.webapp14.mapper;

import es.codeurjc.webapp14.dto.ProductCreateRequestDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "stock", source = "product.stock")
    @Mapping(target = "outOfStock", source = "product.outOfStock")
    @Mapping(target = "sold", source = "product.sold")
    @Mapping(target = "category", source = "product.category")
    ProductDTO toDTO(Product product);

    @Mapping(target = "id", ignore = true) // Cuando creamos un nuevo producto, no necesitamos el id
    @Mapping(target = "reviews", ignore = true) // Ignoramos las reviews
    @Mapping(target = "sizes", ignore = true) // Ignoramos las sizes
    Product toEntity(ProductCreateRequestDTO productCreateRequestDTO);
}
