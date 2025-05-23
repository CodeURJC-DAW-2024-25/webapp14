package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.service.OrderProductService;
import es.codeurjc.webapp14.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import es.codeurjc.webapp14.dto.NewProductRequestDTO;
import es.codeurjc.webapp14.dto.OrderProductDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Endpoints for managing Products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductService orderProductService;

    @Operation(summary = "Get Product", description = "Return a single Product")
    @GetMapping("/{id}")
    public ProductDTO getProductDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {

        return productService.getProductById(id);

    }

    
    @Operation(summary = "Get Product Image", description = "Return a single Product Image")
    @GetMapping("/{id}/image")
	public ResponseEntity<Object> getProductImage(@PathVariable long id) throws SQLException, IOException {

        if(productService.getProductImage(id) == null){
            throw new EntityNotFoundException("Image not found");
        }

		Resource productImage = productService.getProductImage(id);
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				.body(productImage);

	}


    private List<String> categories = new ArrayList<>(
            Arrays.asList("Abrigos", "Camisetas", "Pantalones", "jerséis"));

    @Operation(summary = "Get Products", description = "Return all the Products created")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated products

        data.put("products", productService.getProductsPaginated(page, size));

        // Get total products
        data.put("totalProducts", productService.getTotalProducts());

        // Get total categories
        // Refactor? Categories should be a query instead of hardcoded array?
        data.put("categoriesCount", categories.size());

        // Get total stock
        data.put("totalStock", productService.getTotalStockOfAllProducts());

        // Get number of products with all sizes out of stock
        data.put("totalOutStock", productService.countProductsWithAllSizesOutOfStock());

        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Get out of stock Products", description = "Return all the Products out of stock")
    @GetMapping("/out-of-stock")
    public List<ProductDTO> getOutOfStockProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService.getProductsWithAllSizesOutOfStock();

    }

    @Operation(summary = "Create Product", description = "Create and save a Product on the database")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody NewProductRequestDTO newProductRequestDTO,
    @RequestParam(value = "stock_S", required = false, defaultValue = "0") int stockS,
    @RequestParam(value = "stock_M", required = false, defaultValue = "0") int stockM,
    @RequestParam(value = "stock_L", required = false, defaultValue = "0") int stockL,
    @RequestParam(value = "stock_XL", required = false, defaultValue = "0") int stockXL) {
        try {
            ProductDTO product = create(newProductRequestDTO, stockS, stockM, stockL, stockXL);
            URI location = URI.create("https://localhost:8443/api/v1/products/" + product.id());
            return ResponseEntity.created(location).body(product);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private ProductDTO create(NewProductRequestDTO newProductRequestDTO, int stock_S, int stock_M, int stock_L, int stock_XL) throws SQLException, IOException {
        int stock = stock_S + stock_L + stock_M + stock_XL;

        ProductDTO productDTO = new ProductDTO(null, newProductRequestDTO.name(), newProductRequestDTO.description(), newProductRequestDTO.price(),
        stock, stock == 0, 0,newProductRequestDTO.category(), false, null, null, null);

        ProductDTO newProductDTO = productService.createOrReplaceProduct(null, productDTO, stock_S, stock_M, stock_L, stock_XL);

        return newProductDTO;
    }

    @Operation(summary = "Create Product Image", description = "Create and save an Image for a Product on the database")
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createProductImage(@PathVariable long id,
                                                    @RequestParam MultipartFile imageFile) throws IOException {



        URI location = URI.create("https://localhost:8443/api/v1/products/" + id + "/image");
        productService.createProductImage(id, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Edit Product Image", description = "Edit a created Produc Image")
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceProdcutImage( @PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

     productService.replaceProductImage(id, imageFile.getInputStream(), imageFile.getSize());

     return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Edit Product", description = "Edit a created Product")
    @PutMapping("/{id}")
    public ProductDTO replaceProduct(@RequestBody ProductDTO ProductDTO,
    @RequestParam(value = "stock_S", required = false, defaultValue = "0") int stockS,
    @RequestParam(value = "stock_M", required = false, defaultValue = "0") int stockM,
    @RequestParam(value = "stock_L", required = false, defaultValue = "0") int stockL,
    @RequestParam(value = "stock_XL", required = false, defaultValue = "0") int stockXL,
    @PathVariable Long id) {
        try {
            ProductDTO product = replace(id, ProductDTO, stockS, stockM, stockL, stockXL);
            return product;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new NoSuchElementException();
        }
    }


    private ProductDTO replace(Long id, ProductDTO productDTO, int stock_S, int stock_M, int stock_L, int stock_XL) throws SQLException, IOException {

        ProductDTO newProductDTO = productService.createOrReplaceProduct(id, productDTO, stock_S, stock_M, stock_L, stock_XL);

        return newProductDTO;
    }

    @Operation(summary = "Delete Product Image", description = "Delete a created Product Image")
    @DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deleteProductImage(@PathVariable long id) throws IOException {

		productService.deleteProductImage(id);

		return ResponseEntity.noContent().build();
	}

    @Operation(summary = "Delete Product", description = "Delete a created Product")
    @DeleteMapping("/{id}")
    public ProductDTO deleteProduct(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);

        Boolean deleteOk = true;
        List<OrderProductDTO> orderProducts = orderProductService.getAllOrderProducts();
        for (OrderProductDTO orderProduct : orderProducts) {
            if (orderProduct.product().id().equals(id)) {

                deleteOk = false;
                break;
            }
        }

        if (!deleteOk) {
            throw new NoSuchElementException();
        }

        productService.delete(id);
        return product;
    }
}
