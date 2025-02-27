package es.codeurjc.webapp14.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    Order findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    // Ventas totales (suma del totalPrice de todos los pedidos)
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.isPaid = true")
    BigDecimal getTotalSales();

    // Ventas de hoy (suma del totalPrice de pedidos de hoy)
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.isPaid = true AND DATE(o.createdAt) = CURRENT_DATE")
    BigDecimal getTodaySales();

    // Total de pedidos
    @Query("SELECT COUNT(o) FROM Order o")
    long getTotalOrders();

    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as count " +
            "FROM orders " +
            "WHERE created_at >= CURDATE() - INTERVAL 30 DAY " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)", nativeQuery = true)
    List<Object[]> countOrdersLast30Days();

    Optional<Order> findFirstByUserAndIsPaidFalse(User user);
}
