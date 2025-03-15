package es.codeurjc.webapp14.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public BigDecimal getTotalSales() {
        return orderRepository.getTotalSales();
    }

    public BigDecimal getTodaySales() {
        return orderRepository.getTodaySales();
    }

    public long getTotalOrders() {
        return orderRepository.getTotalOrders();
    }

    public Map<String, List<?>> getOrdersLast30Days() {
        List<Object[]> ordersData = orderRepository.countOrdersLast30Days();

        List<String> orderDates = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        for (Object[] row : ordersData) {
            orderDates.add(row[0].toString());
            orderCounts.add(((Number) row[1]).intValue());
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("dates", orderDates);
        result.put("counts", orderCounts);

        return result;
    }

    public Optional<Order> getUnpaidOrder(User user) {
        return orderRepository.findFirstByUserAndIsPaidFalse(user);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> getOrdersPaginated(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }

    public List<Order> getPaidOrders(User user) {
        return orderRepository.findByUserAndIsPaidTrue(user);
    }
}
