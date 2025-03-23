package es.codeurjc.webapp14.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.model.Product;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId);

    Optional<OrderProduct> findByOrderAndProductAndSize(Order order, Product product, String size);

    @Query("SELECT SUM(op.product.price * op.quantity) FROM OrderProduct op WHERE op.order = :order")
    Double getTotalPriceByOrder(@Param("order") Order order);

    List<OrderProduct> findByProductId(Long id);
}
