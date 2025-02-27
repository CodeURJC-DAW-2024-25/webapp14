package es.codeurjc.webapp14.controllers;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.http.*;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.repositories.OrderRepository;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.services.EmailService;
import es.codeurjc.webapp14.services.OrderProductService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    public String getUserOrders(Model model, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        User user = userService.findById(userId);

        if (user != null) {
            List<Order> orders = orderService.getPaidOrders(user);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            orders.forEach(order -> order.setCreatedAtFormatted(order.getCreatedAt().format(formatter)));

            model.addAttribute("orders", orders);
            model.addAttribute("exists", !orders.isEmpty());
        }

        return "/user_registered/orders";
    }

    @GetMapping("/{id}")
    public String getOrderProductsById(@PathVariable Long id, Model model) {

    Optional<Order> optionalOrder = orderService.getOrderById(id);
    
    if (!optionalOrder.isPresent()) {
        return "redirect:/no-page-error";
    }

    Order order = optionalOrder.get();
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

    @GetMapping("/pdf/{id}")
    // To generate PDF for an order
    public ResponseEntity<byte[]> generateOrderPdf(@PathVariable Long id) throws IOException {
        // The order is searched in the database
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Order order = orderOptional.get();
        // Generate HTML content
        String htmlContent = generateHtmlFromTemplate(order);
        // Convert HTML to PDF
        byte[] pdfBytes = generatePdfFromHtml(htmlContent);
        // Set the response headers for a PDF file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "order_" + id + ".pdf");
        // Returns the PDF along with the headers
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // Method to generate HTML content from the Mustache template
    private String generateHtmlFromTemplate(Order order) throws IOException {
        // Create a MustacheFactory object to compile the template
        MustacheFactory mf = new DefaultMustacheFactory();
        // Load the order_pdf.html template from the resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/order_pdf.html");
        if (inputStream == null) {
            throw new IOException("Template not found: order_pdf.html");
        }
        // Compile the Mustache template
        Mustache mustache = mf.compile(new InputStreamReader(inputStream), "order_pdf");
        // Create a map to store the data to be used in the template
        Map<String, Object> data = new HashMap<>();
        String formattedTotalPrice = String.format("%.2f", order.getTotalPrice());
        data.put("createdAt", order.getCreatedAt());
        data.put("state", order.getState());
        data.put("totalPrice", formattedTotalPrice);
        List<Map<String, Object>> orderProducts = new ArrayList<>();
        for (OrderProduct op : order.getOrderProducts()) {
            Map<String, Object> productData = new HashMap<>();
            productData.put("product.name", op.getProduct().getName());
            productData.put("quantity", op.getQuantity());
            productData.put("product.price", op.getProduct().getPrice());
            productData.put("subtotal", op.getQuantity() * op.getProduct().getPrice());
            orderProducts.add(productData);
        }
        data.put("orderProducts", orderProducts);
        // Render the HTML content by executing the template with the data
        StringWriter writer = new StringWriter();
        mustache.execute(writer, data).flush();
        // Return the rendered HTML
        return writer.toString();
    }

    // Method to generate PDF from the given HTML content
    private byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        // Create an output stream to store the PDF data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Create an ITextRenderer object to render the HTML content into a PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (com.lowagie.text.DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
        // Return the generated PDF
        return outputStream.toByteArray();
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
