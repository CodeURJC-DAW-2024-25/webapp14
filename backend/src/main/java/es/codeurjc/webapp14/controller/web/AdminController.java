package es.codeurjc.webapp14.controller.web;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.service.OrderProductService;
import es.codeurjc.webapp14.service.OrderService;
import es.codeurjc.webapp14.service.ProductService;
import es.codeurjc.webapp14.service.ReviewService;
import es.codeurjc.webapp14.service.UserService;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderProductService orderProductService;

    private List<String> categories = new ArrayList<>(
            Arrays.asList("Abrigos", "Camisetas", "Pantalones", "jerséis"));

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
    }

    @ModelAttribute
    public void addProfileImage(Model model) throws SQLException {
        Optional<User> admin = userService.getAdmin();
        if (admin.isPresent() && admin.get().getProfileImage() != null) {
            model.addAttribute("hasImage", true);
        } else {
            model.addAttribute("hasImage", false);
        }
    }

    @GetMapping("/charts")
    public String showAdminCharts(Model model, HttpServletRequest request) {
        model.addAttribute("totalSales", orderService.getTotalSales());
        model.addAttribute("todaySales", orderService.getTodaySales());
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        model.addAttribute("totalUsers", userService.getAllUsers().size());

        List<Product> topProducts = productService.getTop5BestSellingProducts();

        if (topProducts.size() > 5) {
            topProducts = topProducts.subList(0, 5);
        }

        List<String> productNames = topProducts.stream().map(Product::getName).collect(Collectors.toList());
        List<Integer> productSales = topProducts.stream().map(Product::getSold).collect(Collectors.toList());

        model.addAttribute("productNames", productNames);
        model.addAttribute("productSales", productSales);

        Map<String, List<?>> ordersData = orderService.getOrdersLast30Days();

        model.addAttribute("orderDates", ordersData.get("dates"));
        model.addAttribute("orderCounts", ordersData.get("counts"));

        return "admin/admin_charts";
    }

    @GetMapping("/profile")
    public String showAdminProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User admin = userService.findByEmail(userDetails.getUsername());

        if (admin == null) {
            return "redirect:/login";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("hasImage", admin.getProfileImage() != null);

        return "admin/admin_profile";
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET, RequestMethod.POST })
    public String showAdminProfileEdit(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "surname", required = false, defaultValue = "") String surname,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            @RequestParam(value = "confirmPassword", required = false, defaultValue = "") String confirmPassword,
            @RequestParam(value = "password", required = false, defaultValue = "") String password,
            @RequestParam(value = "currentPassword", required = false, defaultValue = "") String currentPassword,
            @RequestParam(value = "imageUpload", required = false) MultipartFile image,
            Model model,
            HttpServletRequest request) throws Exception {

        Optional<User> existing = userService.getAdmin();
        if (existing.isEmpty()) {

            return "redirect:/admin/profile";
        }

        User existingAdmin = existing.get();

        if ("GET".equals(request.getMethod())) {
            model.addAttribute("admin", existingAdmin);
            model.addAttribute("hasImage", existingAdmin.getProfileImage() != null);

            return "admin/admin_profile_edit";
        }

        Map<String, List<String>> errors = new HashMap<>();

        if (name.isEmpty()) {
            errors.computeIfAbsent("name", k -> new ArrayList<>()).add("El nombre no puede estar vacío");
        }
        if (surname.isEmpty()) {
            errors.computeIfAbsent("surname", k -> new ArrayList<>()).add("El apellido no puede estar vacío");
        }
        if (email.isEmpty()) {
            errors.computeIfAbsent("email", k -> new ArrayList<>()).add("El correo electrónico no puede estar vacío");
        }

        if (currentPassword.isEmpty() && !password.isEmpty()) {
            errors.computeIfAbsent("password", k -> new ArrayList<>()).add("La contraseña no puede estar vacía");
        }
        if (!currentPassword.isEmpty()
                && (!passwordEncoder.matches(currentPassword, existingAdmin.getEncodedPassword()))) {
            errors.computeIfAbsent("currentPassword", k -> new ArrayList<>()).add("La contraseña actual es incorrecta");
        }
        if (!password.isEmpty() && !password.equals(confirmPassword)) {
            errors.computeIfAbsent("newPassword", k -> new ArrayList<>()).add("La nueva contraseña no coincide");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("admin", existingAdmin);
            model.addAttribute("hasImage", existingAdmin.getProfileImage() != null);

            return "admin/admin_profile_edit";
        }

        existingAdmin.setName(name);
        existingAdmin.setSurname(surname);
        existingAdmin.setEmail(email);

        if (!currentPassword.isEmpty() && !password.isEmpty()) {
            String encoded = passwordEncoder.encode(confirmPassword);
            existingAdmin.setEncodedPassword(encoded);
        }

        if (image != null && !image.isEmpty()) {
            userService.saveAdmin(existingAdmin, image);
        } else {
            userService.saveAdmin(existingAdmin);
        }

        return "redirect:/admin/profile";
    }

    @GetMapping("/profile/image")
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
    public String showAdminOrders(@RequestParam(value = "orderId", required = false) Long orderId,
            @RequestParam(value = "state", required = false) Order.State newState,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            HttpServletRequest request) {

        if (request.getMethod().equals("GET")) {
            Page<Order> orderPage = orderService.getOrdersPaginated(page, size);
            List<Order> paidOrders = orderPage.getContent().stream()
                    .filter(Order::getIsPaid) // Filter only paid orders
                    .collect(Collectors.toList());
            List<Order> paid = orderService.getAllOrders().stream()
                    .filter(Order::getIsPaid)
                    .collect(Collectors.toList());
            model.addAttribute("orders", paidOrders);
            model.addAttribute("orderCount", paid.size());
            model.addAttribute("hasMore", orderPage.hasNext());
            model.addAttribute("nextPage", page + 1);
            return "admin/admin_orders";
        }

        Optional<Order> optionalOrder = orderService.getOrderById(orderId);

        Order order = optionalOrder.get();
        if (order != null) {
            order.setState(newState);
            orderService.saveOrder(order);
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/product/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Optional<Product> existproduct = productService.getProductById(id);

        Product product = existproduct.get();
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
            @RequestParam(value = "deleteTry", required = false) Boolean deleteTry,
            Model model) {

        Page<Product> productPage = productService.getProductsPaginated(page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("hasMore", productPage.hasNext());
        model.addAttribute("nextPage", page + 1);

        List<Product> products = productService.getAllProducts();

        model.addAttribute("productCount", products.size());
        model.addAttribute("categoriesCount", categories.size());
        int totalStock = productService.getTotalStockOfAllProducts();
        int totalOutStock = productService.countProductsWithAllSizesOutOfStock();

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("totalOutStock", totalOutStock);
        model.addAttribute("categories", categories);
        model.addAttribute("deleteTry", deleteTry != null ? deleteTry : false);

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
            @RequestParam(value = "stock_S", required = false, defaultValue = "0") int stockS,
            @RequestParam(value = "stock_M", required = false, defaultValue = "0") int stockM,
            @RequestParam(value = "stock_L", required = false, defaultValue = "0") int stockL,
            @RequestParam(value = "stock_XL", required = false, defaultValue = "0") int stockXL,
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

        List<Size> sizes = List.of(
                new Size(Size.SizeName.S, stockS, product),
                new Size(Size.SizeName.M, stockM, product),
                new Size(Size.SizeName.L, stockL, product),
                new Size(Size.SizeName.XL, stockXL, product));

        product.setSizes(sizes);
        product.setStock(stockS + stockM + stockL + stockXL);
        product.setOutOfStock(stockS + stockM + stockL + stockXL == 0);

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/out-of-stock")
    public String showOutOfStockProducts(Model model) {
        List<Product> products = productService.getProductsWithAllSizesOutOfStock();

        model.addAttribute("products", products);
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("categoriesCount", categories.size());

        int totalStock = productService.getTotalStockOfAllProducts();
        int totalOutStock = productService.countProductsWithAllSizesOutOfStock();

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("totalOutStock", totalOutStock);
        model.addAttribute("categories", categories);

        return "admin/admin_products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Product> existproduct = productService.getProductById(id);

        if (!existproduct.isPresent()) {
            return "redirect:/no-page-error";
        }

        Boolean deleteOk = true;
        List<OrderProduct> orderProducts = orderProductService.getAllOrderProducts();
        for (OrderProduct orderProduct : orderProducts) {
            if (orderProduct.getProduct().getId().equals(id)) {
                deleteOk = false;
                break;
            }
        }

        if (!deleteOk) {
            redirectAttributes.addAttribute("deleteTry", true);
        }

        if (deleteOk) {
            productService.delete(id);
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
            @ModelAttribute("product") @Valid Product updatedProduct,
            BindingResult result,
            @RequestParam(value = "removeImage", required = false) boolean removeImage,
            @RequestParam(value = "image", required = false) MultipartFile imageField,
            @RequestParam Map<String, String> stockParams,
            Model model) throws IOException {

        Optional<Product> existproduct = productService.getProductById(id);

        Product existingProduct = existproduct.get();

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());

        if (existingProduct.getSizes() != null) {
            int totalStock = 0;

            for (Size size : existingProduct.getSizes()) {
                String stockKey = "stock_" + size.getName();
                if (stockParams.containsKey(stockKey)) {
                    int newStock = Integer.parseInt(stockParams.get(stockKey));
                    size.setStock(newStock);
                }
                totalStock += size.getStock();
            }

            existingProduct.setStock(totalStock);
            existingProduct.setOutOfStock(totalStock == 0);
        }

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

    @GetMapping("/moreOrdersAdmin")
    public String getMoreAdminOrders(
            @RequestParam int page,
            @RequestParam int size,
            Model model) {

        Page<Order> ordersPage = orderService.getOrdersPaginated(page, size);
        boolean hasMore = page < ordersPage.getTotalPages() - 1;

        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("hasMore", hasMore);

        return "admin/moreOrdersAdmin";
    }

    @PostMapping("/users/accept/{id}")
    public String acceptReview(@PathVariable Long id, Model model) {
        Optional<Review> existreview = reviewService.getReviewById(id);
        ;

        Review review = existreview.get();

        review.setReported(false);
        reviewService.saveReview(review);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteReview(@PathVariable Long id, Model model) {

        Optional<Review> existreview = reviewService.getReviewById(id);
        if (!existreview.isPresent()) {
            return "redirect:/no-page-error";
        }

        reviewService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/ban/{id}")
    public String banUser(@PathVariable Long id, Model model) {
        Optional<User> userConsult = userService.findById(id);

        if (!userConsult.isPresent()) {
            return "redirect:/no-page-error";
        }

        User user = userConsult.get();
        user.setBanned(true);
        user.getReviews().clear();
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable Long id, Model model) {
        Optional<User> userConsult = userService.findById(id);

        if (!userConsult.isPresent()) {
            return "redirect:/no-page-error";
        }

        User user = userConsult.get();
        user.setBanned(false);
        userService.saveUser(user);

        return "redirect:/admin/users";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.findById(id);
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.getOrderById(id);
        orderService.delete(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/user/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
        Optional<User> userConsult = userService.findById(id);

        User user = userConsult.get();
        Blob imageBlob = user.getProfileImage();
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

}
