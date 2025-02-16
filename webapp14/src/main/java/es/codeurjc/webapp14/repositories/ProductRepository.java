package es.codeurjc.webapp14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
