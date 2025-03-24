package es.codeurjc.webapp14.controller.rest;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.UserDTO;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


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
    }

    @GetMapping
    public OrderDTO getCart(@ModelAttribute("userId") long userId) {

        OrderDTO order = orderService.listProducts(userId);

        return order;
    }

    @PostMapping("/{id}")
    public ResponseEntity<OrderDTO> addToCart(@ModelAttribute("userId") long userId, @PathVariable("id") Long id,  @RequestParam("size") String size,
    @RequestParam("quantity") int quantity) {

        UserDTO user = userService.findById(userId);

        ProductDTO product = productService.getProductById(id);

        OrderDTO createdOrder = orderService.addToCart(id, user,product,size,quantity);
        
        URI location = URI.create("https://localhost:8443/api/v1/cart");

        return ResponseEntity.created(location).body(createdOrder);
    }

    @PatchMapping("/{id}")
    public OrderDTO removeFromCart(@PathVariable("id") Long orderProductId, @ModelAttribute("userId") Long userId) {

        OrderDTO order = orderService.deleteOrderProduct(orderProductId, userId);
        
        return order;
    }


    @PatchMapping
    public OrderDTO proccess(@ModelAttribute("userId") Long userId) {

        OrderDTO orderNotProcessed = orderService.listProducts(userId);

        Boolean cannotProcessOrder = orderService.getCannotProcessOrder(orderNotProcessed);

        if (cannotProcessOrder) {
            return null;
        }

        cannotProcessOrder = orderService.procesOrderStock(orderNotProcessed);

        if (cannotProcessOrder) {
            return null;
        }

        orderService.processOrderSizes(orderNotProcessed);

        OrderDTO order = orderService.proccesOrder(userId);

        return order;
    }

}
