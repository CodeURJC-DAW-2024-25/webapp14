package es.codeurjc.webapp14.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOutOfStockTrue();

    List<Product> findTop4ByOrderBySoldDesc();


}



