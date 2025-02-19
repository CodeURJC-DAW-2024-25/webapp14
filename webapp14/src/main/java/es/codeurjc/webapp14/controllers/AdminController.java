package es.codeurjc.webapp14.controllers;

import es.codeurjc.webapp14.services.UserService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.ReviewService;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    private List<String> categories = new ArrayList<>(
            Arrays.asList("ropa-invierno", "ropa-verano", "accesorios", "zapatos"));

    @GetMapping("/admin/profile")
    // To show the admin profile
    public String showAdminProfile(Model model) {
        model.addAttribute("admin", userService.getAdmin());
        model.addAttribute("hasImage", userService.getAdmin().getProfileImage() != null);
        return "admin/admin_profile";
    }

    @RequestMapping(value = "/admin/edit", method = { RequestMethod.GET, RequestMethod.POST })
    // To show the admin profile form and handle admin editing
    public String showAdminProfileEdit(
            @ModelAttribute("admin") @Valid User admin,
            BindingResult result,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam(value = "currentPassword", required = false) String currentPassword,
            @RequestParam(value = "imageUpload", required = false) MultipartFile image,
            Model model,
            HttpServletRequest request) throws Exception {
        // To handle admin editing
        User existingAdmin = userService.getAdmin();
        // If the request is GET only show the form
        if (request.getMethod().equals("GET")) {
            model.addAttribute("admin", existingAdmin);
            model.addAttribute("hasImage", existingAdmin.getProfileImage() != null);
            return "admin/admin_profile_edit";
        }
        // If the request is POST validate the data
        // Check if fields are empty
        if (admin.getName().isEmpty()) {
            result.rejectValue("name", "error.user", "El nombre no puede estar vacío");
        }
        if (admin.getSurname().isEmpty()) {
            result.rejectValue("surname", "error.user", "El apellido no puede estar vacío");
        }
        if (admin.getEmail().isEmpty()) {
            result.rejectValue("email", "error.user", "El correo electrónico no puede estar vacío");
        }
        // Validate current password
        boolean incorrectCurrentPassword = false;
        if (!existingAdmin.getPassword().equals(currentPassword) && !currentPassword.isEmpty()) {
            incorrectCurrentPassword = true;
            result.rejectValue("password", "error.admin", "La contraseña actual es incorrecta");
        }
        // Verify that the new password matches the confirmation password
        if (!confirmPassword.isEmpty() && !admin.getPassword().equals(confirmPassword)) {
            incorrectCurrentPassword = true;
            result.rejectValue("newPassword", "error.admin", "La nueva contraseña no coincide");
        }
        // Verify if all password fields are empty or the current password is empty
        if ((currentPassword.isEmpty() && admin.getPassword().isEmpty() &&
                confirmPassword.isEmpty()) || incorrectCurrentPassword) {
            admin.setPassword(existingAdmin.getPassword());
        }
        // If there are errors, the view is returned with the messages
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
            return "admin/admin_profile_edit";
        }
        // If the new password is valid update it
        if (!currentPassword.isEmpty() && !confirmPassword.isEmpty() && !incorrectCurrentPassword) {
            admin.setPassword(confirmPassword);
        }
        // Save the image if provided
        if (image != null && !image.isEmpty()) {
            userService.saveAdmin(admin, image);
        } else {
            userService.saveAdmin(admin);
        }
        // Redirect to another page if all is correct
        return "redirect:/admin/profile";
    }

    @GetMapping("/admin/profile/image")
    // To recover and return the administrator image
    public ResponseEntity<Object> downloadImage() throws SQLException {
        User admin = userService.getAdmin();
        if (admin.getProfileImage() != null) {
            Blob image = admin.getProfileImage();
            Resource file = new InputStreamResource(image.getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(image.length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/orders")
    // To show the orders
    public String showAdminOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        List<Order> paidOrders = orders.stream()
                .filter(Order::getIsPaid) // Filter only paid orders
                .collect(Collectors.toList());
        model.addAttribute("orders", paidOrders);
        model.addAttribute("orderCount", paidOrders.size());
        return "admin/admin_orders";
    }

    @GetMapping("/product/image/{id}")
    @ResponseBody
    // To show the product image
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        Blob imageBlob = product.getImage();
        if (imageBlob != null) {
            try {
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return ResponseEntity.ok().headers(headers).body(imageBytes);
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        // Add the product list in to the model
        model.addAttribute("productCount", products.size());
        model.addAttribute("categoriesCount", categories.size());
        int totalStock = products.stream()
                .mapToInt(Product::getStock)
                .sum();

        long totalOutStock = products.stream()
                .filter(product -> product.getStock() == 0)
                .count();

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("totalOutStock", totalOutStock);
        model.addAttribute("categories", categories);

        return "admin/admin_products";
    }

    @PostMapping("/admin/products/create")
    public String addProduct(Product product) {

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/out-of-stock")
    public String showOutOfStockProducts(Model model) {
        List<Product> products = productService.getAllProductsOutOfStock();
        // Add the product filtered list in to the model
        model.addAttribute("products", products);
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("categoriesCount", categories.size());
        int totalStock = productService.getAllProducts().stream()
                .mapToInt(Product::getStock)
                .sum();

        long totalOutStock = products.stream()
                .filter(product -> product.getStock() == 0)
                .count();

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("totalOutStock", totalOutStock);
        model.addAttribute("categories", categories);

        return "admin/admin_products";
    }

    @PostMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.getProductById(id);
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/edit/{id}")
    public String updateProduct(Product product) {

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<User> bannedUsers = userService.getAllUsersBanned();


        model.addAttribute("users", users);
        model.addAttribute("userCont", users.size());
        model.addAttribute("bannedUsers", bannedUsers);
        model.addAttribute("bannedUserCont", bannedUsers.size());

        int totalReportedReviews = 0;

        List<User> usersWithReportedReviews = userService.getUsersWithReportedReviews();

        for (User user : usersWithReportedReviews) {
            totalReportedReviews += user.getReports();
        }
        model.addAttribute("usersWithReportedReviews", usersWithReportedReviews);

        model.addAttribute("totalReportedReviews", totalReportedReviews);
        

        return "admin/admin_users";
    }

    @PostMapping("/admin/users/accept/{id}")
    public String acceptReview(@PathVariable Long id, Model model) {
        Review review = reviewService.getReviewById(id);
        review.setReported(false);
        reviewService.saveReview(review);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteReview(@PathVariable Long id, Model model) {
        reviewService.getReviewById(id);
        reviewService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/ban/{id}")
    public String banUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        user.setBanned(true);
        user.getReviews().clear();
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/unban/{id}")
    public String unbanUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        user.setBanned(false);
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

}
