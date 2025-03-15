package es.codeurjc.webapp14.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.codeurjc.webapp14.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

        List<Product> findByOutOfStockTrue();

        List<Product> findTop4ByOrderBySoldDesc();

        List<Product> findTop5ByOrderBySoldDesc();

        Page<Product> findByCategory(String category, Pageable pageable);

        Page<Product> findAll(Pageable pageable);

        List<Product> findByNameContainingIgnoreCase(String name);

        @Query("SELECT p FROM Product p " +
                        "WHERE p.category IN :categories " +
                        "AND p.id NOT IN (" +
                        "   SELECT DISTINCT op.product.id FROM OrderProduct op " +
                        "   WHERE op.order.user.id = :userId" +
                        ")")
        Page<Product> findRecommendedProductsByCategories(@Param("categories") List<String> categories,
                        @Param("userId") Long userId,
                        Pageable pageable);

        @Query("SELECT p FROM Product p WHERE p.category = :category")
        List<Product> findTopProductsByCategory(@Param("category") String category, Pageable pageable);
}
