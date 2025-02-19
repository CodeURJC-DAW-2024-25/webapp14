package es.codeurjc.webapp14.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId);
}
