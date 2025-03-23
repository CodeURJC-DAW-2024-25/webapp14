package es.codeurjc.webapp14.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/index")
public class IndexRestController {

    @Autowired
    private ProductService productService;

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
}
