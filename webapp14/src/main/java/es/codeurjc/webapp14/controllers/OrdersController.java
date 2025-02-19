package es.codeurjc.webapp14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.webapp14.services.OrderProductService;
import es.codeurjc.webapp14.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final OrderProductService orderProductService;

    public OrdersController(OrderService orderService, OrderProductService orderProductService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
    }

    @GetMapping
    public String getUserOrders(Model model) {
        model.addAttribute("orders", orderService.getUserOrders());
        return "/user_registered/orders";
    }

    @GetMapping("/{id}")
    public String getOrderProductById(@PathVariable Long id, Model model) {
        model.addAttribute("orderProducts", orderProductService.getOrderProductsByOrderId(id));
        return "/user_registered/orders_detail";
    }

    // @PostMapping
    // public String createOrderProduct(@ModelAttribute OrderProduct orderProduct) {
    // orderProductService.saveOrderProduct(orderProduct);
    // return "redirect:/orders";
    // }
}
