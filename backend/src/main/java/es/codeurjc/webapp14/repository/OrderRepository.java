package es.codeurjc.webapp14.repository;

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

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.isPaid = true")
    BigDecimal getTotalSales();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.isPaid = true AND DATE(o.createdAt) = CURRENT_DATE")
    BigDecimal getTodaySales();

    @Query("SELECT COUNT(o) FROM Order o")
    long getTotalOrders();

    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as count " +
            "FROM orders " +
            "WHERE created_at >= CURDATE() - INTERVAL 30 DAY " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at)", nativeQuery = true)
    List<Object[]> countOrdersLast30Days();

    Optional<Order> findFirstByUserAndIsPaidFalse(User user);

    List<Order> findByUserAndIsPaidTrue(User user);
}
