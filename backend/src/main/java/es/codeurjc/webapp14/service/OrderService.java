package es.codeurjc.webapp14.service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.codeurjc.webapp14.dto.BasicProductSizeDTO;
import es.codeurjc.webapp14.dto.OrderDTO;
import es.codeurjc.webapp14.dto.OrderProductDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.SizeDTO;
import es.codeurjc.webapp14.dto.UserDTO;
import es.codeurjc.webapp14.mapper.OrderMapper;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order.State;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

@Service
public class OrderService {

    private final SizeService sizeService;

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderProductService orderProductService;
    private final OrderMapper orderMapper;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, UserService userService,
            OrderProductService orderProductService, OrderMapper orderMapper, ProductService productService,
            SizeService sizeService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderProductService = orderProductService;
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.sizeService = sizeService;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderByIdWeb(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order;
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toDTO(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public BigDecimal getTotalSales() {
        return orderRepository.getTotalSales();
    }

    public BigDecimal getTodaySales() {
        return orderRepository.getTodaySales();
    }

    public long getTotalOrders() {
        return orderRepository.getTotalOrders();
    }

    public Map<String, List<?>> getOrdersLast30Days() {
        List<Object[]> ordersData = orderRepository.countOrdersLast30Days();

        List<String> orderDates = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();

        for (Object[] row : ordersData) {
            orderDates.add(row[0].toString());
            orderCounts.add(((Number) row[1]).intValue());
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("dates", orderDates);
        result.put("counts", orderCounts);

        return result;
    }

    public Optional<Order> getUnpaidOrder(User user) {
        return orderRepository.findFirstByUserAndIsPaidFalse(user);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public Page<OrderDTO> getOrdersPaginated(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size)).map(orderMapper::toDTO);
    }

    public List<Order> getPaidOrders(User user) {
        return orderRepository.findByUserAndIsPaidTrue(user);
    }

    public OrderDTO addToCart(Long productId, UserDTO userDTO, ProductDTO productDTO, String size, int quantity) {
        User user = userService.toDomain(userDTO);
        Optional<Order> order = orderRepository.findFirstByUserAndIsPaidFalse(user);
        Product product = productService.toDomain(productDTO);

        Order presentOrder;
        if (!order.isPresent()) {
            presentOrder = new Order(user, State.No_pagado, false);
            saveOrder(presentOrder);
        } else {
            presentOrder = order.get();
        }

        Optional<OrderProduct> existingOrderProduct = orderProductService
                .getOrderProductByOrderAndProductAndSize(presentOrder, product, size);
        OrderProduct orderProduct;

        if (existingOrderProduct.isPresent()) {
            orderProduct = existingOrderProduct.get();
            orderProduct.setQuantity(orderProduct.getQuantity() + quantity);
        } else {
            orderProduct = new OrderProduct(presentOrder, product, size, quantity);
        }

        orderProductService.saveOrderProduct(orderProduct);
        saveOrder(presentOrder);
        System.out.println("CANTIDAD:" + quantity);
        System.out.println("PRECIO" + product.getPrice());

        presentOrder.setTotalPrice(product.getPrice() * quantity);

        saveOrder(presentOrder);

        return toDTO(presentOrder);

    }

    private OrderDTO toDTO(Order order) {
        return orderMapper.toDTO(order);
    }

    public OrderDTO listProducts(Long userId) {

        UserDTO userDTO = userService.findById(userId);
        User user = userService.toDomain(userDTO);

        Optional<Order> unpaidOrder = getUnpaidOrder(user);

        Order order;

        if (!unpaidOrder.isPresent()) {
            order = new Order(user, State.No_pagado, false);

            saveOrder(order);

        } else {
            order = unpaidOrder.get();
        }

        return toDTO(order);
    }

    public BigDecimal getSubTotal(OrderDTO order) {
        return order.totalPrice();

    }

    public BigDecimal getShipping(OrderDTO order) {
        BigDecimal subtotal = order.totalPrice();
        BigDecimal shipping = BigDecimal.ZERO;
        if (subtotal.compareTo(BigDecimal.valueOf(100)) < 0) {
            shipping = BigDecimal.valueOf(5);
        }

        return shipping;
    }

    private Order toDomain(OrderDTO orderDTO) {
        return orderMapper.toDomain(orderDTO);
    }

    public OrderDTO deleteOrderProduct(Long orderProductId, Long userId) {
        UserDTO userConsult = userService.findById(userId);
        User user = userService.toDomain(userConsult);

        Optional<Order> unpaidOrder = getUnpaidOrder(user);

        if (unpaidOrder.isPresent()) {
            Optional<OrderProduct> orderProductToRemove = orderProductService.getOrderProductById(orderProductId);

            if (orderProductToRemove.isPresent() && !orderProductToRemove.get().getOrder().getIsPaid()
                    && orderProductToRemove.get().getOrder().getUser().getId().equals(userId)) {
                unpaidOrder.get().setTotalPrice(-1 * orderProductToRemove.get().getQuantity()
                        * orderProductToRemove.get().getProduct().getPrice());
                saveOrder(unpaidOrder.get());

                orderProductService.deleteOrderProduct(orderProductToRemove.get());
                return toDTO(unpaidOrder.get());
            }
        }

        throw new AccessDeniedException("You do not have permission to delete this order product");
    }

    public List<OrderDTO> getOrdersFromUser(Long userId) {
        UserDTO userConsult = userService.findById(userId);
        User user = userService.toDomain(userConsult);

        List<Order> orders = getPaidOrders(user);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        orders.forEach(order -> order.setCreatedAtFormatted(order.getCreatedAt().format(formatter)));

        return toDTOs(orders);
    }

    private List<OrderDTO> toDTOs(List<Order> orders) {
        return orderMapper.toDTOs(orders);
    }

    public OrderDTO getOrderProductById(Long orderId, Long userId) {
        userService.findById(userId);
        return getOrderById(orderId);
    }

    public OrderDTO proccesOrder(Long userId) {
        UserDTO userDTO = userService.findById(userId);
        User user = userService.toDomain(userDTO);
        Optional<Order> unpaidOrder = orderRepository.findFirstByUserAndIsPaidFalse(user);

        Order order = unpaidOrder.get();
        order.setIsPaid(true);
        order.setState(State.Pagado);

        BigDecimal totalPrice = order.getTotalPrice();

        if (totalPrice.compareTo(BigDecimal.valueOf(100)) < 0) {
            BigDecimal newTotal = totalPrice.add(BigDecimal.valueOf(10));
            order.setTotalPriceWithShipping(newTotal.doubleValue());
        }

        /*
         * 
         * // For sending the email
         * try {
         * ordersController.sendOrderEmail(order.getId());
         * } catch (Exception e) {
         * System.err.println("Error enviando el correo: " + e.getMessage());
         * }
         * 
         */

        saveOrder(order);

        Order newOrder = new Order(user, State.No_pagado, false);
        saveOrder(newOrder);

        return toDTO(order);
    }

    public List<OrderDTO> getAllPaidOrders() {
        List<Order> orders = orderRepository.findIsPaidTrue();
        return toDTOs(orders);
    }

    public Page<OrderDTO> getOrdersPaidPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findPaidOrders(pageable)
                .map(orderMapper::toDTO);
    }

    public OrderDTO deleteOrder(Long id) {
        OrderDTO order = getOrderById(id);
        delete(id);
        return order;
    }

    public OrderDTO editOrder(Long orderId, Order.State newState) {
        OrderDTO orderDTO = getOrderById(orderId);
        Order order = toDomain(orderDTO);

        order.setUser(userService.findUserById(orderDTO.userId()));
        order.setState(newState);
        List<OrderProduct> orderProducts = orderDTO.orderProducts().stream()
                .map(orderProductDTO -> {
                    OrderProduct orderProduct = orderProductService.toDomain(orderProductDTO);
                    orderProduct.setOrder(order);
                    return orderProduct;
                })
                .collect(Collectors.toList());

        order.setOrderProducts(orderProducts);

        saveOrder(order);

        return toDTO(order);
    }

    public Boolean getCannotProcessOrder(OrderDTO order) {
        boolean cannotProcessOrder = false;

        for (OrderProductDTO orderProduct : order.orderProducts()) {

            int quantity = orderProduct.quantity();
            String size = orderProduct.size();
            BasicProductSizeDTO product = orderProduct.product();
            List<SizeDTO> productSizes = product.sizes();

            Optional<SizeDTO> productSize = productSizes.stream()
                    .filter(s -> s.name().equals(size))
                    .findFirst();

            if (productSize.isPresent()) {
                SizeDTO sizeObj = productSize.get();
                int availableStock = sizeObj.stock();

                if (quantity > availableStock) {
                    cannotProcessOrder = true;
                    break;
                }
            }
        }

        return cannotProcessOrder;
    }

    public Boolean procesOrderStock(OrderDTO orderNotProcessed) {

        Order unpaidOrder = toDomain(orderNotProcessed);

        boolean cannotProcessOrder = false;

        for (OrderProduct orderProduct : unpaidOrder.getOrderProducts()) {
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
                    return true;
                } else {
                    int updatedStock = availableStock - quantity;
                    sizeObj.setStock(updatedStock);

                    if (sizeObj.getProduct() == null) {
                        sizeObj.setProduct(product);
                    }

                    sizeService.saveSize(sizeObj);
                    product.incrementSold(orderProduct.getQuantity());
                }
            }
        }

        return cannotProcessOrder;
    }

    public void processOrderSizes(OrderDTO orderNotProcessed) {

        Order unpaidOrder = toDomain(orderNotProcessed);

        for (OrderProduct orderProduct : unpaidOrder.getOrderProducts()) {
            Product product = orderProduct.getProduct();
            boolean allSizesOutOfStock = true;
            for (Size size : product.getSizes()) {
                if (size.getProduct() == null) {
                    size.setProduct(product);
                    sizeService.saveSize(size);
                }

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
    }

}
