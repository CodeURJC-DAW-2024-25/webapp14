package es.codeurjc.webapp14.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Size;

public interface SizeRepository extends JpaRepository<Size, Long> {

    @Query("SELECT COALESCE(SUM(s.stock), 0) FROM Size s")
    int getTotalStockOfAllProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE NOT EXISTS (SELECT s FROM Size s WHERE s.product = p AND s.stock > 0)")
    int countProductsWithAllSizesOutOfStock();

    @Query("SELECT p FROM Product p WHERE NOT EXISTS (SELECT s FROM Size s WHERE s.product = p AND s.stock > 0)")
    List<Product> findProductsWithAllSizesOutOfStock();



}
