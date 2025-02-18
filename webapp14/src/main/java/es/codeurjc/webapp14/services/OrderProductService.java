package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.repositories.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {
    private final OrderProductRepository orderProductRepository;

    public OrderProductService(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    public OrderProduct getOrderProductById(Long id) {
        return orderProductRepository.findById(id).orElse(null);
    }

    public OrderProduct saveOrderProduct(OrderProduct orderProduct) {
        return orderProductRepository.save(orderProduct);
    }

    public void deleteOrderProduct(Long id) {
        orderProductRepository.deleteById(id);
    }
}
