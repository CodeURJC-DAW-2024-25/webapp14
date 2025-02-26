package es.codeurjc.webapp14.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    Order findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findFirstByUserAndIsPaidFalse(User user);
}
