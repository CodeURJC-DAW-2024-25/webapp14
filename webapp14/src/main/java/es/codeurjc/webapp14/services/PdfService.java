package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.repositories.OrderRepository;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.util.*;

@Service
public class PdfService {

    @Autowired
    private OrderRepository orderRepository;

    // Method to generate a PDF file for a given order
    public ResponseEntity<byte[]> generateOrderPdf(Long id) throws IOException {
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
        MustacheFactory mf = new DefaultMustacheFactory();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/order_pdf.html");
        if (inputStream == null) {
            throw new IOException("Template not found: order_pdf.html");
        }
        Mustache mustache = mf.compile(new InputStreamReader(inputStream), "order_pdf");
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
        StringWriter writer = new StringWriter();
        mustache.execute(writer, data).flush();
        return writer.toString();
    }

    // Method to generate PDF from the given HTML content
    private byte[] generatePdfFromHtml(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (com.lowagie.text.DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
        return outputStream.toByteArray();
    }

}
