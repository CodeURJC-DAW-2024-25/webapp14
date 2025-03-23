package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/orders")
public class UserOrdersRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

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
            model.addAttribute("userId", 0);
            model.addAttribute("admin", false);
        }
    }

    @GetMapping
    public List<OrderDTO> getUserOrders(@ModelAttribute("userId") long userId) {

        List<OrderDTO> orders = orderService.getOrdersFromUser(userId);

        return orders;

    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id, @ModelAttribute("userId") long userId) {

        OrderDTO order = orderService.getOrderProductById(id,userId);

        if (!order.isPaid()) {
            return null;
        }

        if (userId == 0 || !order.userId().equals(userId)) {
            return null;
        }

        return order;
    }

}
