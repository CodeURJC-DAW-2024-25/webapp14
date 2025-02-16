// package es.codeurjc.webapp14.controllers;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import es.codeurjc.webapp14.model.Product;
// import es.codeurjc.webapp14.model.Review;
// import es.codeurjc.webapp14.model.User;
// import es.codeurjc.webapp14.model.UserReports;

// @Controller
// public class AdminController {

// private List<Product> products = new ArrayList<>(); // Lista de productos
// private List<String> categories = new ArrayList<>(
// Arrays.asList("ropa-invierno", "ropa-verano", "accesorios", "zapatos"));

// private List<User> users = new ArrayList<>(); // Lista de usuarios

// // Constructor para inicializar algunos productos (puedes quitar esto si usas
// // una base de datos)
// public AdminController() {
// products.add(new Product(8754676, "Abrigos", "Elegancia y calidez", 50.00,
// "Ropa de Invierno", 20,
// List.of(
// new Review("Usuario1", "Excelente calidad, muy recomendable.", 5, true,
// 8754676),
// new Review("Usuario2", "Muy cómodo y abrigado.", 4, true, 8754676),
// new Review("Usuario3", "El color es un poco distinto al de la foto.", 3,
// true, 8754676))));

// products.add(new Product(6758492, "Sudaderas", "Comodidad y estilo", 35.00,
// "Sudadera", 0,
// List.of(
// new Review("Usuario4", "Excelente calidad y muy cómoda.", 5, true, 6758492),
// new Review("Usuario5",
// "Es súper calentita y cómoda, perfecta para el invierno. La volvería a
// comprar.", 5,
// false, 6758492),
// new Review("Usuario1",
// "Me gusta el diseño, pero la tela es un poco más delgada de lo que
// esperaba.", 4, true,
// 6758492))));

// products.add(new Product(9856321, "Chaquetas", "Diseño elegante", 15.00,
// "Chaquetas", 30,
// List.of(
// new Review("Usuario2",
// "Me encantó el material, es muy suave y cómodo. Perfecta para el día a día.",
// 5, false,
// 9856321),
// new Review("Usuario4",
// "El diseño es bonito, pero la talla me quedó un poco ajustada. Recomiendo
// pedir una talla más grande.",
// 4, false, 9856321),
// new Review("Usuario5",
// "Después de un par de lavadas, los colores siguen intactos. ¡Buena calidad!",
// 5, false,
// 9856321))));

// users.add(new User(0763774, "Juan Pérez", "juanperez@email.com", "Calle Falsa
// 123, Madrid", 3,
// "/images/undraw_profile.svg"));
// users.add(new User(8768342, "María López", "marialopez@email.com", "Av.
// Siempre Viva 742, Barcelona", 5,
// "/images/undraw_profile.svg"));
// users.add(new User(6736822, "Carlos Ramírez", "carlosramirez@email.com", "C/
// Gran Vía 123, Valencia", 8,
// "/images/undraw_profile.svg"));
// users.add(new User(2754556, "Laura Martínez", "lauramartinez@email.com",
// "Paseo del Prado 56, Sevilla", 4,
// "/images/undraw_profile.svg"));
// users.add(new User(4566756, "Pedro Gómez", "pedrogomez@email.com", "Av. de la
// Constitución 101, Bilbao",
// 6, "/images/undraw_profile.svg"));

// }

// @GetMapping("/admin/products")
// public String showProducts(Model model) {
// model.addAttribute("products", products); // Añadir la lista de productos al
// modelo
// for (Product product : products) {
// // Agregar un atributo 'outOfStock' si el stock es 0
// product.setOutOfStock(product.getStock() == 0);
// }
// model.addAttribute("products", products);
// model.addAttribute("productCount", products.size());
// model.addAttribute("categoriesCount", categories.size());
// int totalStock = products.stream()
// .mapToInt(Product::getStock)
// .sum();

// long totalOutStock = products.stream()
// .filter(product -> product.getStock() == 0)
// .count();

// model.addAttribute("totalStock", totalStock);
// model.addAttribute("totalOutStock", totalOutStock);
// model.addAttribute("categories", categories);

// System.out.println("Lista de productos en /admin/products:");
// for (Product product : products) {
// System.out.println(product.getName() + " - Stock: " + product.getStock());
// }

// return "admin/admin_products"; // Devuelve la vista admin/admin_products.html
// }

// @PostMapping("/admin/products")
// public String addProduct(@RequestParam(required = false) String name,
// @RequestParam(required = false) String description,
// @RequestParam(required = false) Double price,
// @RequestParam(required = false) String category,
// @RequestParam(required = false) Integer stock,
// RedirectAttributes redirectAttributes) {

// System.out.println("Método addProduct ejecutado");

// // Validación manual
// if (name == null || name.trim().isEmpty() ||
// description == null || description.trim().isEmpty() ||
// price == null || price <= 0 ||
// category == null || category.trim().isEmpty() ||
// stock == null || stock < 0) {

// System.out.println("Error en la validación");
// redirectAttributes.addFlashAttribute("error",
// "Todos los campos deben estar completos y el precio debe ser mayor a 0.");
// return "redirect:/admin/products";
// }

// System.out.println("Producto válido: " + name);

// // Crear el producto si los datos son válidos
// Product newProduct = new Product(generateUniqueId(), name, description,
// price, category, stock,
// new ArrayList<>());
// products.add(newProduct);

// redirectAttributes.addFlashAttribute("success", "Producto agregado
// correctamente.");
// return "redirect:/admin/products";
// }

// @PostMapping("/admin/categories/add")
// public String addCategory(@RequestParam("categoryName") String categoryName,
// Model model) {
// // Verificar si la categoría no está vacía
// if (!categoryName.isEmpty()) {
// categories.add(categoryName); // Añadir la categoría a la lista de categorías
// }
// // Redirigir de vuelta a la vista de productos
// model.addAttribute("categories", categories);
// return "redirect:/admin/products"; // Redirigir para actualizar la lista de
// categorías
// }

// @GetMapping("/admin/products/out-of-stock")
// public String showOutOfStockProducts(Model model) {
// System.out.println("Lista de productos en /admin/products/out-of-stock:");
// for (Product product : products) {
// System.out.println(product.getName() + " - Stock: " + product.getStock());
// }

// List<Product> outOfStockProducts = new ArrayList<>();
// for (Product product : products) {
// if (product.getStock() == 0) {
// outOfStockProducts.add(product);
// }
// }

// // Reutiliza la misma lógica de estadísticas
// int totalStock = products.stream()
// .mapToInt(Product::getStock)
// .sum();
// long totalOutStock = outOfStockProducts.size();

// model.addAttribute("products", outOfStockProducts);
// model.addAttribute("productCount", products.size());
// model.addAttribute("categoriesCount", categories.size());
// model.addAttribute("totalStock", totalStock);
// model.addAttribute("totalOutStock", totalOutStock);
// model.addAttribute("categories", categories);

// System.out.println("Productos sin stock encontrados: " + totalOutStock);

// return "admin/admin_products";
// }

// // Método para actualizar el producto (POST)
// @PostMapping("/admin/products/edit/{id}")
// public String updateProduct(@PathVariable int id,
// @RequestParam String name,
// @RequestParam Double price,
// @RequestParam String category,
// @RequestParam int stock) {

// // Buscar el producto a actualizar
// Product product = products.stream()
// .filter(p -> p.getId() == id)
// .findFirst()
// .orElse(null);

// // Si el producto existe, actualiza sus detalles
// if (product != null) {
// product.setName(name);
// product.setPrice(price);
// product.setCategory(category);
// product.setStock(stock);
// }

// // Redirigir de nuevo a la lista de productos
// return "redirect:/admin/products";
// }

// // Método para eliminar un producto por ID
// @PostMapping("/admin/products/delete/{id}")
// public String deleteProduct(@PathVariable int id) {
// // Buscar el producto por ID y eliminarlo
// products.removeIf(product -> product.getId() == id);

// // Redirigir a la lista de productos actualizada
// return "redirect:/admin/products";
// }

// // Método para generar un ID único (esto puedes adaptarlo según tu lógica de
// // base de datos)
// private int generateUniqueId() {
// return (int) (Math.random() * 10000000); // Ejemplo de generación de ID
// aleatorio
// }

// @GetMapping("/admin/users")
// public String getUsers(Model model) {

// model.addAttribute("users", users);
// model.addAttribute("userCont", users.size());

// List<UserReports> userReportsList = new ArrayList<>();

// // Recorrer productos y filtrar reseñas reportadas
// for (Product product : products) {
// for (Review review : product.getReviews()) {
// if (review.isReported()) {
// // Buscar si el usuario ya está en la lista
// Optional<UserReports> userReportsOpt = userReportsList.stream()
// .filter(userReports ->
// userReports.getUsername().equals(review.getUsername()))
// .findFirst();

// if (userReportsOpt.isPresent()) {
// userReportsOpt.get().getReviews().add(review);
// } else {
// UserReports userReports = new UserReports(review.getUsername());
// userReports.getReviews().add(review);
// userReportsList.add(userReports);
// }
// }
// }
// }

// // Eliminar usuarios sin reseñas reportadas
// userReportsList.removeIf(userReports -> userReports.getReviews().isEmpty());

// // Calcular el número de reseñas reportadas por cada usuario
// for (UserReports userReports : userReportsList) {
// userReports.setReviewCount(userReports.getReviews().size()); // Agregar un
// campo de conteo
// }

// // Enviar la lista a la vista
// model.addAttribute("reportedReviewsByUser", userReportsList);
// return "admin/admin_users";
// }

// @GetMapping("/admin/orders")
// public String getOrders(Model model) {
// return "admin/admin_orders";
// }

// }
