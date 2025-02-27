package es.codeurjc.webapp14.controllers;

import es.codeurjc.webapp14.services.UserService;
import es.codeurjc.webapp14.services.OrderService;
import es.codeurjc.webapp14.services.ProductService;
import es.codeurjc.webapp14.services.ReviewService;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.Size.SizeName;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


    private List<String> categories = new ArrayList<>(
            Arrays.asList("abrigos", "camisetas", "pantalones", "jerséis"));

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

    @GetMapping("/charts")
    public String showAdminCharts(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return "redirect:/login";
        }

        return "admin/admin_charts";
    }

    @GetMapping("/profile")
    public String showAdminProfile(Model model, HttpServletRequest request) {


        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("userEmail");

        if (email == null) {
            return "redirect:/login";
        }

        User admin = userService.findByEmail(email);

        System.out.println("Roles: " + admin.getRoles());


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

        System.out.println("Entro");
        Optional<User> existing = userService.getAdmin();
        if (existing.isEmpty()) {
            System.out.println("User vacío");

            return "redirect:/admin/profile";
        }

        User existingAdmin = existing.get();

        if ("GET".equals(request.getMethod())) {
            System.out.println("Soy get");

            model.addAttribute("admin", existingAdmin);
            model.addAttribute("hasImage", existingAdmin.getProfileImage() != null);
            return "admin/admin_profile_edit";
        }

        Map<String, List<String>> errors = new HashMap<>();

        System.out.println("Soy post");
        System.out.println("admin name: " + existingAdmin.getName() );
        System.out.println("Soy post");

        // Validaciones
        if (name.isEmpty()) {
            errors.computeIfAbsent("name", k -> new ArrayList<>()).add("El nombre no puede estar vacío");
        }
        if (surname.isEmpty()) {
            errors.computeIfAbsent("surname", k -> new ArrayList<>()).add("El apellido no puede estar vacío");
        }
        if (email.isEmpty()) {
            errors.computeIfAbsent("email", k -> new ArrayList<>()).add("El correo electrónico no puede estar vacío");
        }

        System.out.println("admin passow: " + existingAdmin.getName() );


        if(currentPassword.isEmpty()){
            System.out.println("Password vacía");
            errors.computeIfAbsent("password", k -> new ArrayList<>()).add("La contraseña no puede estar vacía");
        }

        if (!currentPassword.isEmpty() && (!passwordEncoder.matches(currentPassword, existingAdmin.getEncodedPassword()))) {
            errors.computeIfAbsent("currentPassword", k -> new ArrayList<>()).add("La contraseña actual es incorrecta");
        }
        if (!password.isEmpty() && !password.equals(confirmPassword)) {
            errors.computeIfAbsent("newPassword", k -> new ArrayList<>()).add("La nueva contraseña no coincide");
        }

        if (!errors.isEmpty()) {
            System.out.println("Hay errores");

            model.addAttribute("errors", errors);
            model.addAttribute("admin", existingAdmin);
            model.addAttribute("hasImage", existingAdmin.getProfileImage() != null);
            return "admin/admin_profile_edit";
        }

        System.out.println("No hay errores");

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
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
    
        if (!optionalOrder.isPresent()) {
            return "redirect:/no-page-error";
        }
    
        Order order = optionalOrder.get();        
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
        Optional <Product> existproduct = productService.getProductById(id);

 

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

    @RequestMapping(value = "/products/create", method = {RequestMethod.GET, RequestMethod.POST})
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
            new Size(Size.SizeName.XL, stockXL, product)
        );

        product.setSizes(sizes);
        product.setStock(stockS + stockM + stockL + stockXL);
        product.setOutOfStock(stockS + stockM + stockL + stockXL == 0);


        productService.saveProduct(product);

        return "redirect:/admin/products";
    }


    @GetMapping("/products/out-of-stock")
    public String showOutOfStockProducts(Model model) {
        List<Product> products = productService.getProductsWithAllSizesOutOfStock();
        // Add the product filtered list in to the model
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
    public String deleteProduct(@PathVariable Long id) {
        Optional <Product> existproduct = productService.getProductById(id);

        if(!existproduct.isPresent()){
            return "redirect:/no-page-error";
        }
        productService.delete(id);
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
    
        Optional <Product> existproduct = productService.getProductById(id);

        if(!existproduct.isPresent()){
            return "redirect:/no-page-error";
        }
        
        Product existingProduct= existproduct.get();
            
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

    @PostMapping("/users/accept/{id}")
    public String acceptReview(@PathVariable Long id, Model model) {
        Optional <Review> existreview = reviewService.getReviewById(id);;

        if(!existreview.isPresent()){
            return "redirect:/no-page-error";
        }

        Review review = existreview.get();

        review.setReported(false);
        reviewService.saveReview(review);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteReview(@PathVariable Long id, Model model) {

        Optional <Review> existreview = reviewService.getReviewById(id);;

        if(!existreview.isPresent()){
            return "redirect:/no-page-error";
        }

        
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
