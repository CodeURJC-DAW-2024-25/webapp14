package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.io.*;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") // Retrieves the email from application properties
    private String mailFrom;

    // Method for sending emails
    public void sendOrderEmail(User user, Order order) {
        try {
            // Create a new MIME email message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            // Set email sender and subject
            helper.setFrom(mailFrom);
            helper.setTo(user.getEmail());
            helper.setSubject("Detalles de tu pedido #" + order.getId());
            // Create a model with order details to be used in the email template
            Map<String, Object> model = new HashMap<>();
            model.put("userName", user.getName());
            model.put("orderDate", order.getCreatedAt().toString());
            model.put("totalPrice", order.getTotalPrice());
            List<Map<String, Object>> orderProducts = order.getOrderProducts().stream().map(op -> {
                Map<String, Object> productData = new HashMap<>();
                productData.put("productName", op.getProduct().getName());
                productData.put("quantity", op.getQuantity());
                productData.put("price", op.getProduct().getPrice());
                productData.put("subtotal", op.getSubtotalPrice());
                return productData;
            }).toList();
            model.put("orderProducts", orderProducts);
            // Load the Mustache template
            InputStreamReader reader = new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream("templates/order_email.html"),
                    StandardCharsets.UTF_8);
            // Compile and process the Mustache template with the provided model
            MustacheFactory mf = new DefaultMustacheFactory();
            com.github.mustachejava.Mustache mustacheTemplate = mf.compile(reader, "order_email");
            StringWriter writer = new StringWriter();
            mustacheTemplate.execute(writer, model).flush();
            String htmlContent = writer.toString();
            // Set the email content as HTML
            helper.setText(htmlContent, true);
            // Send the email
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
