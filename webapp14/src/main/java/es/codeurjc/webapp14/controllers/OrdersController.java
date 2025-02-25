package es.codeurjc.webapp14.controllers;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.services.OrderProductService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final UserService userService;

    public OrdersController(OrderService orderService, OrderProductService orderProductService,
            UserService userService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.userService = userService;
    }

    @GetMapping
    public String getUserOrders(Model model, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        User user = userService.findById(userId);

        if (user != null) {
            // Obtén los pedidos del usuario
            List<Order> orders = orderService.getUserOrders(user);

            // Formatear la fecha para cada pedido antes de pasarlos al modelo
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            orders.forEach(order -> order.setCreatedAtFormatted(order.getCreatedAt().format(formatter)));

            model.addAttribute("orders", orders);
        }

        return "/user_registered/orders";
    }

    @GetMapping("/{id}")
    public String getOrderProductsById(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);

        if (order != null) {
            BigDecimal subtotal = order.getTotalPrice();

            // Calcular el coste de envío (5 euros si el subtotal es menor a 100)
            BigDecimal shipping = BigDecimal.ZERO;

            if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
                shipping = BigDecimal.valueOf(5);
            }

            // Calcular el total (subtotal + envío)
            BigDecimal total = subtotal.add(shipping);

            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProductService.getOrderProductsByOrderId(id));
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shipping", shipping);
            model.addAttribute("total", total);
        }

        return "/user_registered/orders_detail";
    }

    // @PostMapping
    // public String createOrderProduct(@ModelAttribute OrderProduct orderProduct) {
    // orderProductService.saveOrderProduct(orderProduct);
    // return "redirect:/orders";
    // }
}
