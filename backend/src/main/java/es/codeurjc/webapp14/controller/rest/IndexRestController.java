package es.codeurjc.webapp14.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Index", description = "Endpoints for managing Products from the Index")
public class IndexRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

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


    @Operation(summary = "Get index Products", description = "Return the products based on the implemented algorithm")
    @GetMapping
    public Map<String, Object> getIndexData(HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size, @ModelAttribute("userId") long userId) {
        Map<String, Object> data = new HashMap<>();

        System.out.println("USERID:" +userId);

        if (userId == 0){
            data.put("bestSellingProducts", productService.getAllProductsSold());
        }

        else{
            userService.findById(userId);
            data.put("recommendedProducts", productService.getRecommendedProductsBasedOnLastOrder(userId, page, size));
        }

        return data;

    }

    @Operation(summary = "Get Charts", description = "Return the information showed in the charts")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/charts")
    public ResponseEntity<Map<String, Object>> getChartsData() {
        Map<String, Object> data = new HashMap<>();

        // Get today's sales
        data.put("todaySales", orderService.getTodaySales());

        // Get total sales
        data.put("totalSales", orderService.getTotalSales());

        // Get total orders
        data.put("totalOrders", orderService.getTotalOrders());

        // Get all users
        data.put("totalUsers", userService.getAllUsers().size());

        // Get top 5 best selling products
        data.put("top5BestSellingProducts", productService.getTop5BestSellingProducts());

        // Get orders last 30 days
        data.put("ordersLast30Days", orderService.getOrdersLast30Days());

        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Get category Products", description = "Return the products that belongs to the category")
    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> getCategoryData(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page) {
        Map<String, Object> data = new HashMap<>();

        List<String> validCategories = List.of("camisetas", "pantalones", "abrigos", "jerseys");

        if (!validCategories.contains(category.toLowerCase())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Category not found", "category", category));        }

        // Get products by category
        data.put("products", productService.getProductsByCategory(category, page));

        // Get category name
        data.put("category", category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase());

        return ResponseEntity.ok(data);
    }


    @Operation(summary = "Get search Products", description = "Return Products based on the search information")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getSearchData(HttpServletRequest request,
            @RequestParam(value = "query", required = false) String query) {
        Map<String, Object> data = new HashMap<>();

        if (query != null && !query.isEmpty()) {
            List<BasicProductDTO> products = productService.searchProductsByName(query);
            data.put("productsSearch", products);

            return ResponseEntity.ok(data);
        }

        return ResponseEntity.notFound().build();
    }
}
