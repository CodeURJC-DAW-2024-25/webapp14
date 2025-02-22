package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.repositories.OrderRepository;
import es.codeurjc.webapp14.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    public List<Product> searchProductsByName(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    

    public List<Product> getRecommendedProductsBasedOnLastOrder(Long userId) {
        Order lastOrder = orderRepository.findFirstByUserIdOrderByCreatedAtDesc(userId);

        List<String> categories = lastOrder.getOrderProducts().stream()
            .map(op -> op.getProduct().getCategory())
            .distinct()
            .collect(Collectors.toList());

        return productRepository.findRecommendedProductsByCategories(categories, userId, PageRequest.of(0, 12));
    }
}
