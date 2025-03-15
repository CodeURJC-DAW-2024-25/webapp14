package es.codeurjc.webapp14.controller.web;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repository.OrderRepository;
import es.codeurjc.webapp14.service.EmailService;
import es.codeurjc.webapp14.service.OrderProductService;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.model.Order;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final UserService userService;

    public OrdersController(OrderService orderService, OrderProductService orderProductService,
            UserService userService) {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isLogged = auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String);
        model.addAttribute("logged", isLogged);

        if (isLogged) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());

            model.addAttribute("userName", user.getName());
            model.addAttribute("userId", user.getId());
            model.addAttribute("admin", user.getRoles().contains("ADMIN"));
        } else {
            model.addAttribute("userName", null);
            model.addAttribute("userId", null);
            model.addAttribute("admin", false);
        }

        model.addAttribute("query", "");
    }

    @GetMapping
    public String getUserOrders(Model model, @ModelAttribute("userId") Long userId) {
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> userConsult = userService.findById(userId);

        if (userConsult.isPresent()) {
            User user = userConsult.get();
            List<Order> orders = orderService.getPaidOrders(user);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            orders.forEach(order -> order.setCreatedAtFormatted(order.getCreatedAt().format(formatter)));

            model.addAttribute("orders", orders);
            model.addAttribute("exists", !orders.isEmpty());
        } else {
            return "redirect:/no-page-error";
        }

        return "/user_registered/orders";
    }

    @GetMapping("/{id}")
    public String getOrderProductsById(@PathVariable Long id, Model model, @ModelAttribute("userId") Long userId) {

        Optional<Order> optionalOrder = orderService.getOrderById(id);

        Optional<User> user = userService.findById(userId);

        if (!optionalOrder.isPresent()) {
            return "redirect:/no-page-error";
        }

        if (userId == null || user == null || !optionalOrder.get().getUser().getId().equals(userId)) {
            return "access_error";
        }

        Order order = optionalOrder.get();
        if (order != null) {
            BigDecimal subtotal = order.getTotalPrice();

            BigDecimal shipping = BigDecimal.ZERO;

            if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
                shipping = BigDecimal.valueOf(5);
            }

            BigDecimal total = subtotal.add(shipping);

            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProductService.getOrderProductsByOrderId(id));
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shipping", shipping);
            model.addAttribute("total", total);
        }

        return "/user_registered/orders_detail";
    }

    @GetMapping("/email/{id}")
    // To send an email when doing a purchase
    public ResponseEntity<String> sendOrderEmail(@PathVariable Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            User user = order.getUser();
            emailService.sendOrderEmail(user, order);
            return ResponseEntity.ok("El correo se ha enviado exitosamente a " + user.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El pedido no se ha encontrado");
        }
    }

}
