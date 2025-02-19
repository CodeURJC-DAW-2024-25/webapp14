package es.codeurjc.webapp14.services;

import java.util.List;

import org.springframework.stereotype.Service;

import es.codeurjc.webapp14.model.Order;
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

    public List<Order> getUserOrders() {
        return orderRepository.findAll();
    }
}
