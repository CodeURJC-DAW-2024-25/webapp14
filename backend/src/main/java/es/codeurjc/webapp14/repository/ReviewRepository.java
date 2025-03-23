package es.codeurjc.webapp14.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long id);

    Page<Review> findByProduct(Product product, Pageable pageable);

}
