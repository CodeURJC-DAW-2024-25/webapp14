package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.repository.OrderRepository;
import es.codeurjc.webapp14.repository.ProductRepository;
import es.codeurjc.webapp14.repository.SizeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SizeRepository sizeRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProductsOutOfStock() {
        return productRepository.findByOutOfStockTrue();
    }

    public List<Product> getAllProductsSold() {
        return productRepository.findTop4ByOrderBySoldDesc();
    }

    public List<Product> getTop5BestSellingProducts() {
        return productRepository.findTop5ByOrderBySoldDesc();
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

    public Page<Product> getRecommendedProductsBasedOnLastOrder(Long userId, int page, int size) {
        Order lastOrder = orderRepository.findFirstByUserIdOrderByCreatedAtDesc(userId);

        List<String> categories = lastOrder.getOrderProducts().stream()
                .map(op -> op.getProduct().getCategory())
                .distinct()
                .collect(Collectors.toList());

        return productRepository.findRecommendedProductsByCategories(categories, userId, PageRequest.of(page, size));
    }

    public Page<Product> getProductsPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    public int getTotalStockOfAllProducts() {
        return sizeRepository.getTotalStockOfAllProducts();
    }

    public int countProductsWithAllSizesOutOfStock() {
        return sizeRepository.countProductsWithAllSizesOutOfStock();
    }

    public List<Product> getProductsWithAllSizesOutOfStock() {
        return sizeRepository.findProductsWithAllSizesOutOfStock();
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public int getTotalStock() {
        return productRepository.findAll().stream()
                .mapToInt(Product::getStock)
                .sum();
    }

    public long getOutOfStockProductsCount() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() == 0)
                .count();
    }
}
