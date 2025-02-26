package es.codeurjc.webapp14.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repositories.OrderRepository;

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

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }



    public Optional<Order> getUnpaidOrder(User user) {
        return orderRepository.findFirstByUserAndIsPaidFalse(user);
    }
}
