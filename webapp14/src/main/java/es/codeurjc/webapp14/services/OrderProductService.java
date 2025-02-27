package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.repositories.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;

    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    public List<OrderProduct> getOrderProductsByOrderId(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    public OrderProduct saveOrderProduct(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProduct(Long id) {
        orderProductRepository.deleteById(id);
    }

    public Optional<OrderProduct> getOrderProductByOrderAndProductAndSize(Order order, Product product, String size) {
        return orderProductRepository.findByOrderAndProductAndSize(order, product, size);
    }

    public Optional<OrderProduct> getOrderProductById(Long orderProductId) {
        return orderProductRepository.findById(orderProductId);
    }

    public void deleteOrderProduct(OrderProduct orderProduct) {
        orderProductRepository.delete(orderProduct);
    }

    public Double getTotalPriceByOrder(Order order) {
        Double totalPrice = orderProductRepository.getTotalPriceByOrder(order);
        return (totalPrice != null) ? totalPrice : 0.0; // Maneja posibles valores nulos
    }
    
}
