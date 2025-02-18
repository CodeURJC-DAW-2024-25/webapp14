package es.codeurjc.webapp14.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
