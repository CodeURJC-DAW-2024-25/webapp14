package es.codeurjc.webapp14.controllers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order.State;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.services.OrderProductService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.SizeService;
import es.codeurjc.webapp14.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private final ProductService productService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final OrderProductService orderProductService;

    @Autowired
    private final SizeService sizeService;


    public CartController(ProductService productService, UserService userService, OrderService orderService, OrderProductService orderProductService, SizeService sizeService) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.sizeService = sizeService;
    }


    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean logged = (Boolean) session.getAttribute("logged");
        String userName = (String) session.getAttribute("userName");
        Long sessionUserId = (Long) session.getAttribute("userId");
        Boolean admin = session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin");

        if (logged != null && logged) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", userName);
            model.addAttribute("admin", admin);
           
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false);
        }

        model.addAttribute("query", "");

    }

    @GetMapping
    public String listProducts(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long idUser = (Long) session.getAttribute("userId");
        User user = userService.findById(idUser);

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);


        if (!unpaidOrder.isPresent()) {
            System.out.println("No hay carrito");
            Order order = new Order(user,State.No_pagado,false);
            orderService.saveOrder(order);

            return "user_registered/cart";
        }
        System.out.println("Hay carrito");


        boolean cannotProcessOrder = false;

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
                    break;
                }
            }
        }

        System.out.println("Antes");


        BigDecimal subtotal = unpaidOrder.get().getTotalPrice();

        System.out.println("Después");

        // Calcular el coste de envío (5 euros si el subtotal es menor a 100)
        BigDecimal shipping = BigDecimal.ZERO;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
            shipping = BigDecimal.valueOf(5);
        }

        // Calcular el total (subtotal + envío)
        BigDecimal total = subtotal.add(shipping);

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);



        model.addAttribute("orderNotProcessed", cannotProcessOrder);

        model.addAttribute("products", productService.getAllProductsSold());
        model.addAttribute("orderProducts", unpaidOrder.get().getOrderProducts());
        model.addAttribute("orderProductsEmpty", unpaidOrder == null || unpaidOrder.get().getOrderProducts().isEmpty());


        return "user_registered/cart";
    }


    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") String productPrice,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("size") String size,
            Model model, HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        Long idUser = (Long) session.getAttribute("userId");
        User user = userService.findById(idUser);
        Product product = productService.getProductById(productId);

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);
        
        Order order;
        if (!unpaidOrder.isPresent()){
            order = new Order(user,State.No_pagado,false);
            orderService.saveOrder(order);
            System.out.println("Nueva cesta creada");
        }
        else{
            System.out.println("Ya había cesta");
            order = unpaidOrder.get();
        }

        Optional<OrderProduct> existingOrderProduct = orderProductService.getOrderProductByOrderAndProductAndSize(order, product, size);

        OrderProduct orderProduct;

        if (existingOrderProduct.isPresent()) {
            orderProduct = existingOrderProduct.get();
            orderProduct.setQuantity(orderProduct.getQuantity() + 1);
        } else {
            orderProduct = new OrderProduct(order, product, size, 1);
        }


        orderProductService.saveOrderProduct(orderProduct);
        orderService.saveOrder(order);


        BigDecimal subtotal = unpaidOrder.get().getTotalPrice();

        // Calcular el coste de envío (5 euros si el subtotal es menor a 100)
        BigDecimal shipping = BigDecimal.ZERO;

        if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
            shipping = BigDecimal.valueOf(5);
        }

        // Calcular el total (subtotal + envío)
        BigDecimal total = subtotal.add(shipping);

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);;

        model.addAttribute("orderProducts", orderProductService.getOrderProductsByOrderId(order.getId()));
        model.addAttribute("orderProductsEmpty", orderProductService.getOrderProductsByOrderId(order.getId()).isEmpty());

        return "user_registered/cart";
    }
    

    @PostMapping("/process-order")
    public String procesarPago(HttpSession session, Model model) {

        Long idUser = (Long) session.getAttribute("userId");
        User user = userService.findById(idUser);

        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);

        if (!unpaidOrder.isPresent()) {
            return "user_registered/cart";
        }

        boolean cannotProcessOrder = false;

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

                // Comprobar si hay suficiente stock
                if (quantity > availableStock) {
                    cannotProcessOrder = true;  // No se puede procesar la orden si hay productos sin stock
                } else {
                    // Si hay suficiente stock, actualizar el stock
                    int updatedStock = availableStock - quantity;
                    sizeObj.setStock(updatedStock);
                    sizeService.saveSize(sizeObj);
                }
            }
        }

        // Si no se puede procesar el pedido, devolver true al modelo
        if (cannotProcessOrder) {
            model.addAttribute("orderNotProcessed", true);
            return "/cart";  // Redirigir al carrito para que el usuario vea el error
        }

        // Si todo está bien con el stock, procesar el pago
        Order order = unpaidOrder.get();
        order.setIsPaid(true);
        order.setState(State.Pagado);

        orderService.saveOrder(order);

        Order newOrder = new Order(user,State.No_pagado,false);
        orderService.saveOrder(newOrder);

        // Redirigir a la página de órdenes
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String removeFromCart(@PathVariable("id") Long orderProductId, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        User user = userService.findById(userId);
        Optional<Order> unpaidOrder = orderService.getUnpaidOrder(user);

        if (unpaidOrder.isPresent()) {
            Optional<OrderProduct> orderProductToRemove = orderProductService.getOrderProductById(orderProductId);

            if (orderProductToRemove.isPresent()) {
                orderProductService.deleteOrderProduct(orderProductToRemove.get());
            }
        }

        return "redirect:/cart";
    }



}
