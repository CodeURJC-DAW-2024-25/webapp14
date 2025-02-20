package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.repositories.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import es.codeurjc.webapp14.model.Review;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Product> getAllProductsOutOfStock() {
        return productRepository.findByOutOfStockTrue();
    }

    public List<Product> getAllProductsSold() {
        return productRepository.findTop4ByOrderBySoldDesc();
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getProductsByCategory(String category, int page) {
        int pageSize = 4;
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findByCategory(category.toUpperCase(), pageable);
    }
    
}
