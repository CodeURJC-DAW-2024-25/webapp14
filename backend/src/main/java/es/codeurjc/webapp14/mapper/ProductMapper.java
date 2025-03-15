package es.codeurjc.webapp14.mapper;

import es.codeurjc.webapp14.dto.ProductCreateRequestDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private SizeMapper sizeMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    public ProductDTO toDTO(Product product, boolean includeSizes, boolean includeReviews) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setOutOfStock(product.isOutOfStock());
        dto.setSold(product.getSold());
        dto.setImageBool(product.getImageBool());

        // Set image URL if product has an image
        if (product.getImageBool()) {
            dto.setImageUrl("/api/products/" + product.getId() + "/image");
        }

        if (includeSizes && product.getSizes() != null) {
            dto.setSizes(product.getSizes().stream()
                    .map(sizeMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        if (includeReviews && product.getReviews() != null) {
            dto.setReviews(product.getReviews().stream()
                    .map(reviewMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public ProductDTO toDTO(Product product) {
        return toDTO(product, true, true); // By default, include both
    }

    public List<ProductDTO> toDTOs(Collection<Product> products) {
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Product toEntity(ProductCreateRequestDTO productCreateRequestDTO) {
        if (productCreateRequestDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setName(productCreateRequestDTO.getName());
        product.setDescription(productCreateRequestDTO.getDescription());
        product.setPrice(productCreateRequestDTO.getPrice());
        product.setStock(productCreateRequestDTO.getStock());
        product.setCategory(productCreateRequestDTO.getCategory());
        product.setOutOfStock(productCreateRequestDTO.getStock() == 0);
        product.setSold(0);

        return product;
    }
}
