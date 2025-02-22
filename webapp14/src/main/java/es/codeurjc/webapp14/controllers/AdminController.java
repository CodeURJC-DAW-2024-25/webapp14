package es.codeurjc.webapp14.controllers;

import es.codeurjc.webapp14.services.UserService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.ReviewService;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/admin")
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

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean logged = (Boolean) session.getAttribute("logged");
        String userName = (String) session.getAttribute("userName");
        Boolean admin = session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin");

        if (logged != null && logged) {
            model.addAttribute("logged", true);
            model.addAttribute("userName", userName);
            model.addAttribute("admin", admin);
        } else {
            model.addAttribute("logged", false);
            model.addAttribute("admin", false);

        }

    }

    @GetMapping("/profile")
    public String showAdminProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return "redirect:/login";
        }

        User admin = userService.findByEmail(email);

        model.addAttribute("admin", admin);
        model.addAttribute("hasImage", admin.getProfileImage() != null);
        return "admin/admin_profile";

    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET, RequestMethod.POST })
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
        Optional<User> existing = userService.getAdmin();
        if (existing.isPresent()) {
            User existingAdmin = existing.get();
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
        } else {
            return "redirect:/admin/profile";
        }
    }

    @GetMapping("/profile/image")
    // To recover and return the administrator image
    public ResponseEntity<Object> downloadImage() throws SQLException {
        Optional<User> exits = userService.getAdmin();
        if (exits.isPresent()) {
            User admin = exits.get();
            if (admin.getProfileImage() != null) {
                Blob image = admin.getProfileImage();
                Resource file = new InputStreamResource(image.getBinaryStream());
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .contentLength(image.length()).body(file);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/orders", method = { RequestMethod.GET, RequestMethod.POST })
    // To show the orders
    public String showAdminOrders(@RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "state", required = false) Order.State newState, Model model,
            HttpServletRequest request) {
        // If the request is GET
        if (request.getMethod().equals("GET")) {
            List<Order> orders = orderService.getAllOrders();
            List<Order> paidOrders = orders.stream()
                    .filter(Order::getIsPaid) // Filter only paid orders
                    .collect(Collectors.toList());
            model.addAttribute("orders", paidOrders);
            model.addAttribute("orderCount", paidOrders.size());
            return "admin/admin_orders";
        }
        // If the request is POST
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setState(newState);
            orderService.saveOrder(order);
        }
        return "redirect:/admin/orders";
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

    @GetMapping("/products")
    public String showProducts(@RequestParam(defaultValue = "0") int page, 
                           @RequestParam(defaultValue = "10") int size, 
                           Model model) {

        Page<Product> productPage = productService.getProductsPaginated(page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("hasMore", productPage.hasNext());
        model.addAttribute("nextPage", page + 1);

        List<Product> products = productService.getAllProducts();

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

    @GetMapping("/moreProductsAdmin") 
    public String getMoreAdminProducts(
            @RequestParam int page, 
            @RequestParam int size, 
            Model model) { 

        Page<Product> productsPage = productService.getProductsPaginated(page, size);
        boolean hasMore = page < productsPage.getTotalPages() - 1;

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "admin/moreProductAdmin";
    }

    @RequestMapping(value = "/products/create", method = { RequestMethod.GET, RequestMethod.POST })
    public String addProduct(@ModelAttribute("product") @Valid Product product,
            BindingResult result,
            @RequestParam(value = "imageUpload", required = false) MultipartFile image,
            Model model) throws IOException {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getFieldErrors().stream()
                    .collect(Collectors.groupingBy(FieldError::getField,
                            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList()))));
            return "admin/products/create";
        }

        if (image != null && !image.isEmpty()) {
            product.setImage(BlobProxy.generateProxy(image.getInputStream(), image.getSize()));
            product.setImageBool(true);
        } else {
            product.setImageBool(false);
        }

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/out-of-stock")
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

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.getProductById(id);
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
            @ModelAttribute("product") @Valid Product updatedProduct,
            BindingResult result,
            @RequestParam(value = "removeImage", required = false) boolean removeImage,
            @RequestParam(value = "image", required = false) MultipartFile imageField,
            Model model) throws IOException {

        Product existingProduct = productService.getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setStock(updatedProduct.getStock());

        // Image
        if (removeImage) {
            existingProduct.setImage(null);
            existingProduct.setImageBool(false);
        } else if (imageField != null && !imageField.isEmpty()) {
            existingProduct.setImage(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
            existingProduct.setImageBool(true);
        }

        productService.saveProduct(existingProduct);

        return "redirect:/admin/products";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(defaultValue = "0") int page, 
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(defaultValue = "0") int reportedPage,
                        Model model) {


        Page<User> usersPage = userService.getUsersPaginated(page, size);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("hasMore", usersPage.hasNext());
        model.addAttribute("nextPage", page + 1);

        List<User> users = userService.getAllUsers();
        List<User> bannedUsers = userService.getAllUsersBanned();

        model.addAttribute("userCont", users.size());
        model.addAttribute("bannedUsers", bannedUsers);
        model.addAttribute("bannedUserCont", bannedUsers.size());

        int totalReportedReviews = 0;

        Page<User> reportedUsersPage = userService.getUsersWithReportedReviewsPaginated(reportedPage, size);

        model.addAttribute("usersWithReportedReviews", reportedUsersPage.getContent());
        model.addAttribute("reportedHasMore", reportedUsersPage.hasNext());
        model.addAttribute("reportedNextPage", reportedPage + 1);

        List<User> usersWithReportedReviews = userService.getUsersWithReportedReviews();

        for (User user : usersWithReportedReviews) {
            totalReportedReviews += user.getReports();
        }

        model.addAttribute("totalReportedReviews", totalReportedReviews);

        return "admin/admin_users";
    }

    @GetMapping("/moreUsersAdmin") 
    public String getMoreAdminUsers(
            @RequestParam int page, 
            @RequestParam int size, 
            Model model) { 

        Page<User> usersPage = userService.getUsersPaginated(page, size);
        boolean hasMore = page < usersPage.getTotalPages() - 1;

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "admin/moreUsersAdmin";
    }

    @GetMapping("/moreUsersReviewsAdmin") 
    public String getMoreAdminUsersReviews(
            @RequestParam int reportedPage, 
            @RequestParam int size, 
            Model model) { 

        Page<User> reportedUsersPage = userService.getUsersWithReportedReviewsPaginated(reportedPage, size);
        boolean hasMore = reportedPage < reportedUsersPage.getTotalPages() - 1;

        model.addAttribute("usersWithReportedReviews", reportedUsersPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "admin/moreUsersReviewsAdmin";
    }


    @PostMapping("/users/accept/{id}")
    public String acceptReview(@PathVariable Long id, Model model) {
        Review review = reviewService.getReviewById(id);
        review.setReported(false);
        reviewService.saveReview(review);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteReview(@PathVariable Long id, Model model) {
        reviewService.getReviewById(id);
        reviewService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/ban/{id}")
    public String banUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        user.setBanned(true);
        user.getReviews().clear();
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        user.setBanned(false);
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

}
