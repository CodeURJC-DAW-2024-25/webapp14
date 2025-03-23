package es.codeurjc.webapp14.controller.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final OrderService orderService;

    public CartController(ProductService productService, UserService userService, OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
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
    public String listProducts(@ModelAttribute("userId") Long userId, Model model) {
  
        OrderDTO order = orderService.listProducts(userId);

        /*

        UserDTO user = userService.findById(userId);

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);

        if (!unpaidOrder.isPresent()) {
            Order order = new Order(user, State.No_pagado, false);

            orderService.saveOrder(order);
            System.out.println("No hay carrito");
            return "redirect:/cart";
        }

        boolean cannotProcessOrder = false;

        for (OrderProduct orderProduct : unpaidOrder.get().getOrderProducts()) {
            int quantity = orderProduct.getQuantity();
            String size = orderProduct.getSize();
            Product product = orderProduct.getProduct();

            if (product == null) {
                return "redirect:/no-page-error";
            }

            Optional<Size> productSize = product.getSizes().stream()
                    .filter(s -> s.getName().toString().equals(size))
                    .findFirst();

            if (productSize.isPresent()) {
                Size sizeObj = productSize.get();
                int availableStock = sizeObj.getStock();

                if (quantity > availableStock) {
                    cannotProcessOrder = true;
                    break;
                }
            }
        }

        */

        BigDecimal subtotal = orderService.getSubTotal(order);
        BigDecimal shipping = orderService.getShipping(order);
        BigDecimal total = subtotal.add(shipping);

        Boolean cannotProcessOrder;

        if (order.orderProducts().isEmpty()){
            cannotProcessOrder = false;
        }
        else{
            cannotProcessOrder = orderService.getCannotProcessOrder(order);
        }


        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);
        model.addAttribute("orderNotProcessed", cannotProcessOrder);
        //model.addAttribute("products", productService.getAllProductsSold());
        model.addAttribute("orderProducts", order.orderProducts());
        model.addAttribute("orderProductsEmpty",  order.orderProducts().isEmpty());

        return "user_registered/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") String productPrice,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("size") String size,
            @RequestParam("quantity") int quantity,
            Model model, @ModelAttribute("userId") Long userId) {

        UserDTO user = userService.findById(userId);

        ProductDTO product = productService.getProductById(productId);

        orderService.addToCart(productId,user,product,size,quantity);



        /*

        OrderDTO unpaidOrder = orderService.getUnpaidOrder(user);

        Order order;
        if (!unpaidOrder.isPresent()) {
            order = new Order(user, State.No_pagado, false);
            orderService.saveOrder(order);
        } else {
            order = unpaidOrder.get();
        }

        Optional<OrderProduct> existingOrderProduct = orderProductService.getOrderProductByOrderAndProductAndSize(order,
                product, size);

        OrderProduct orderProduct;

        if (existingOrderProduct.isPresent()) {
            orderProduct = existingOrderProduct.get();
            orderProduct.setQuantity(orderProduct.getQuantity() + quantity);
        } else {
            orderProduct = new OrderProduct(order, product, size, quantity);
        }

        orderProductService.saveOrderProduct(orderProduct);
        orderService.saveOrder(order);

        order.setTotalPrice(product.getPrice() * quantity);

        BigDecimal subtotal = order.getTotalPrice();

        BigDecimal shipping = BigDecimal.ZERO;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
            shipping = BigDecimal.valueOf(5);
        }

        BigDecimal total = subtotal.add(shipping);

        orderService.saveOrder(order);

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);
        ;

        model.addAttribute("orderProducts", orderProductService.getOrderProductsByOrderId(order.getId()));
        model.addAttribute("orderProductsEmpty",
                orderProductService.getOrderProductsByOrderId(order.getId()).isEmpty());

        */

        /*
        BigDecimal subtotal = orderService.getSubTotal(order);
        BigDecimal shipping = orderService.getShipping(order);
        BigDecimal total = subtotal.add(shipping);
        Boolean cannotProcessOrder = false;

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);
        ;

        model.addAttribute("orderProducts", order.orderProducts());
        model.addAttribute("orderProductsEmpty", order.orderProducts().isEmpty());

        */
        return "redirect:/cart";

    }

    @PostMapping("/process-order")
    public String processPayment(@ModelAttribute("userId") Long userId, Model model) {

        OrderDTO orderNotProcessed = orderService.listProducts(userId);

        Boolean cannotProcessOrder = orderService.getCannotProcessOrder(orderNotProcessed);

        if (cannotProcessOrder) {
            model.addAttribute("orderNotProcessed", true);
            return "redirect:/cart";
        }

        cannotProcessOrder = orderService.procesOrderStock(orderNotProcessed);

        if (cannotProcessOrder) {
            model.addAttribute("orderNotProcessed", true);
            return "redirect:/cart";
        }

        orderService.processOrderSizes(orderNotProcessed);

        orderService.proccesOrder(userId);



        

        /*

        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> userConsult = userService.findById(userId);

        if (!userConsult.isPresent()) {
            return "redirect:/no-page-error";
        }

        User user = userConsult.get();

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);

        if (!unpaidOrder.isPresent()) {
            return "redirect:/cart";
        }

        if(unpaidOrder.get().getOrderProducts().isEmpty()){
            return "redirect:/cart";
        }

        boolean cannotProcessOrder = false;

        for (OrderProduct orderProduct : unpaidOrder.get().getOrderProducts()) {
            int quantity = orderProduct.getQuantity();
            String size = orderProduct.getSize();
            Product product = orderProduct.getProduct();

            if (product == null) {
                return "redirect:/no-page-error";
            }

            Optional<Size> productSize = product.getSizes().stream()
                    .filter(s -> s.getName().toString().equals(size))
                    .findFirst();

            if (productSize.isPresent()) {
                Size sizeObj = productSize.get();
                int availableStock = sizeObj.getStock();

                if (quantity > availableStock) {
                    cannotProcessOrder = true;
                    break;
                }
            }
        }

        if (cannotProcessOrder) {
            model.addAttribute("orderNotProcessed", true);
            return "redirect:/cart";
        }

        for (OrderProduct orderProduct : unpaidOrder.get().getOrderProducts()) {
            int quantity = orderProduct.getQuantity();
            String size = orderProduct.getSize();
            Product product = orderProduct.getProduct();

            Optional<Size> productSize = product.getSizes().stream()
                    .filter(s -> s.getName().toString().equals(size))
                    .findFirst();

            if (productSize.isPresent()) {
                Size sizeObj = productSize.get();
                int availableStock = sizeObj.getStock();

                if (quantity > availableStock) {
                    cannotProcessOrder = true;
                } else {
                    int updatedStock = availableStock - quantity;
                    sizeObj.setStock(updatedStock);
                    sizeService.saveSize(sizeObj);
                    product.incrementSold(orderProduct.getQuantity());
                }
            }
        }

        if (cannotProcessOrder) {
            model.addAttribute("orderNotProcessed", true);
            return "redirect:/cart";
        }

        for (OrderProduct orderProduct : unpaidOrder.get().getOrderProducts()) {
            Product product = orderProduct.getProduct();
            boolean allSizesOutOfStock = true;
            for (Size size : product.getSizes()) {
                if (size.getStock() > 0) {
                    allSizesOutOfStock = false;
                    break;
                }
            }
            if (allSizesOutOfStock) {
                product.setOutOfStock(true);
                productService.saveProduct(product);
            }
        }

        Order order = unpaidOrder.get();
        order.setIsPaid(true);
        order.setState(State.Pagado);

        // For sending the email
        try {
            ordersController.sendOrderEmail(order.getId());
        } catch (Exception e) {
            System.err.println("Error enviando el correo: " + e.getMessage());
        }

        orderService.saveOrder(order);

        Order newOrder = new Order(user, State.No_pagado, false);
        orderService.saveOrder(newOrder);
        */

        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String removeFromCart(@PathVariable("id") Long orderProductId, @ModelAttribute("userId") Long userId,
            Model model) {


        orderService.deleteOrderProduct(orderProductId, userId);

        /*
        Optional<User> userConsult = userService.findById(userId);


        User user = userConsult.get();
        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);

        if (unpaidOrder.isPresent()) {
            Optional<OrderProduct> orderProductToRemove = orderProductService.getOrderProductById(orderProductId);

            if (orderProductToRemove.isPresent()) {
                unpaidOrder.get().setTotalPrice(-1 * orderProductToRemove.get().getQuantity()
                        * orderProductToRemove.get().getProduct().getPrice());
                orderService.saveOrder(unpaidOrder.get());

                orderProductService.deleteOrderProduct(orderProductToRemove.get());
            }
        }
            */

        return "redirect:/cart";
    }

}
