package es.codeurjc.webapp14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
