package es.codeurjc.webapp14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.services.OrderProductService;

@Controller
@RequestMapping("/order-products")
public class OrderProductController {
    private final OrderProductService orderProductService;

    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    @GetMapping
    public String getAllOrderProducts(Model model) {
        model.addAttribute("orderProducts", orderProductService.getAllOrderProducts());
        return "orderProducts/list";
    }

    @GetMapping("/{id}")
    public String getOrderProductById(@PathVariable Long id, Model model) {
        model.addAttribute("orderProduct", orderProductService.getOrderProductById(id));
        return "orderProducts/detail";
    }

    @PostMapping
    public String createOrderProduct(@ModelAttribute OrderProduct orderProduct) {
        orderProductService.saveOrderProduct(orderProduct);
        return "redirect:/order-products";
    }

    @DeleteMapping("/{id}")
    public String deleteOrderProduct(@PathVariable Long id) {
        orderProductService.deleteOrderProduct(id);
        return "redirect:/order-products";
    }
}
