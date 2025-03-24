package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.NewProductRequestDTO;
import es.codeurjc.webapp14.dto.OrderProductDTO;

import es.codeurjc.webapp14.service.OrderProductService;
import es.codeurjc.webapp14.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;



@RestController
@RequestMapping("/api/v1/admin/products")
public class AdminProductsRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderProductService orderProductService;

    private List<String> categories = new ArrayList<>(
            Arrays.asList("Abrigos", "Camisetas", "Pantalones", "jerséis"));

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAdminProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> data = new HashMap<>();

        // Get paginated products
        // Refactor? products paginated has hasMore and nextPage attributes?. No, but we
        // can implement it.
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

    @GetMapping("/out-of-stock")
    public List<ProductDTO> getOutOfStockProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return productService.getProductsWithAllSizesOutOfStock();

    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody NewProductRequestDTO newProductRequestDTO,
    @RequestParam(value = "stock_S", required = false, defaultValue = "0") int stockS,
    @RequestParam(value = "stock_M", required = false, defaultValue = "0") int stockM,
    @RequestParam(value = "stock_L", required = false, defaultValue = "0") int stockL,
    @RequestParam(value = "stock_XL", required = false, defaultValue = "0") int stockXL) {
        try {
            ProductDTO product = create(newProductRequestDTO, stockS, stockM, stockL, stockXL);
            URI location = URI.create("https://localhost:8443/api/v1/products" + product.id());
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


    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createProductImage(@PathVariable long id,
                                                    @RequestParam MultipartFile imageFile) throws IOException {

        URI location = URI.create("https://localhost:8443/api/v1/products/" + id + "/image");
        productService.createProductImage(id, location, imageFile.getInputStream(), imageFile.getSize());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replaceProdcutImage( @PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {

     productService.replaceProductImage(id, imageFile.getInputStream(), imageFile.getSize());

     return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> replaceProduct(@RequestBody ProductDTO ProductDTO,
    @RequestParam(value = "stock_S", required = false, defaultValue = "0") int stockS,
    @RequestParam(value = "stock_M", required = false, defaultValue = "0") int stockM,
    @RequestParam(value = "stock_L", required = false, defaultValue = "0") int stockL,
    @RequestParam(value = "stock_XL", required = false, defaultValue = "0") int stockXL,
    @PathVariable Long id) {
        try {
            ProductDTO product = replace(id, ProductDTO, stockS, stockM, stockL, stockXL);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(product.id()).toUri();
            return ResponseEntity.created(location).body(product);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private ProductDTO replace(Long id, ProductDTO productDTO, int stock_S, int stock_M, int stock_L, int stock_XL) throws SQLException, IOException {

        ProductDTO newProductDTO = productService.createOrReplaceProduct(id, productDTO, stock_S, stock_M, stock_L, stock_XL);

        return newProductDTO;
    }

    @DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deleteProductImage(@PathVariable long id) throws IOException {

		productService.deleteProductImage(id);

		return ResponseEntity.noContent().build();
	}



    @DeleteMapping("/{id}")
    public ProductDTO deleteProduct(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);

        Boolean deleteOk = true;
        List<OrderProductDTO> orderProducts = orderProductService.getAllOrderProducts();
        for (OrderProductDTO orderProduct : orderProducts) {
            System.out.println("FOR");
            if (orderProduct.product().id().equals(id)) {
                System.out.println("INCLUIDO");

                deleteOk = false;
                break;
            }
        }

        if (!deleteOk) {
            return null;
        }

        productService.delete(id);
        return product;
    }
}
