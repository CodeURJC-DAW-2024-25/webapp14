package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.model.OrderProduct;
import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Product.CategoryType;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.User;
import es.codeurjc.webapp14.model.Order.State;
import es.codeurjc.webapp14.repositories.ProductRepository;
import es.codeurjc.webapp14.repositories.ReviewRepository;
import es.codeurjc.webapp14.repositories.SizeRepository;
import es.codeurjc.webapp14.repositories.UserRepository;
import es.codeurjc.webapp14.repositories.OrderRepository;
import es.codeurjc.webapp14.repositories.OrderProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.sql.Blob;
import java.sql.SQLException;

import jakarta.transaction.Transactional;
import javax.sql.rowset.serial.SerialBlob;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final SizeRepository sizeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataInitializer(ProductRepository productRepository, UserRepository userRepository,
            ReviewRepository reviewRepository, OrderRepository orderRepository,
            OrderProductRepository orderProductRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Inicializando datos...");
        initializeProducts();
        initializeUsers();
        initializeReviews();
        initializeOrders();
        logger.info("Datos inicializados correctamente.");
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            try {
                logger.info("Cargando productos y tallas...");

                // ABRIGOS
                Blob imageBytes1 = loadImage("images/abrigos/abrigo1.webp");
                Product product1 = new Product("Trench tecnico",
                        "Trench técnico con abrigo acolchado interior desmontable.", 79.95, imageBytes1, 10,
                        CategoryType.ABRIGOS);
                productRepository.save(product1);

                Blob imageBytes2 = loadImage("images/abrigos/abrigo2.webp");
                Product product2 = new Product("Trench tecnico 2 en 1 ",
                        "Trench técnico con abrigo acolchado interior desmontable.", 79.95, imageBytes2, 0,
                        CategoryType.ABRIGOS);
                productRepository.save(product2);

                Blob imageBytes3 = loadImage("images/abrigos/abrigo3.webp");
                Product product3 = new Product("Parka Desmonatble Water Repellent",
                        "Parka relaxed fit confeccionada en tejido técnico que repele agua en contacto.", 89.95,
                        imageBytes3, 10, CategoryType.ABRIGOS);
                productRepository.save(product3);

                Blob imageBytes4 = loadImage("images/abrigos/abrigo4.webp");
                Product product4 = new Product("Abrigo con lana",
                        "Abrigo straight fit confeccionado en tejido con lana. Cuello con solapas de muesca y manga larga acabada en puño con botones.",
                        69.95, imageBytes4, 10, CategoryType.ABRIGOS);
                productRepository.save(product4);

                Blob imageBytes5 = loadImage("images/abrigos/abrigo5.webp");
                Product product5 = new Product("Anorak Water and Wind Protection",
                        "Anorak resistente al agua y al viento con aislamiento térmico para climas fríos.", 69.95,
                        imageBytes5, 10, CategoryType.ABRIGOS);
                productRepository.save(product5);

                Blob imageBytes6 = loadImage("images/abrigos/abrigo6.webp");
                Product product6 = new Product("Trench cinturon",
                        "Trench de cuello solapa y manga larga acabada con trabilla y botón.", 59.95, imageBytes6, 0,
                        CategoryType.ABRIGOS);
                productRepository.save(product6);

                Blob imageBytes7 = loadImage("images/abrigos/abrigo7.webp");
                Product product7 = new Product("Abrigo acolchado capucha",
                        "Cazadora de cuello subido con capucha ajustable con cordones y manga larga.", 39.95,
                        imageBytes7, 10, CategoryType.ABRIGOS);
                productRepository.save(product7);

                Blob imageBytes8 = loadImage("images/abrigos/abrigo8.webp");
                Product product8 = new Product("Abrigo Soft",
                        "Abrigo de cuello solapa y manga larga con hombreras. Bolsillos delanteros.", 39.95,
                        imageBytes8, 10, CategoryType.ABRIGOS);
                productRepository.save(product8);

                Blob imageBytes9 = loadImage("images/abrigos/abrigo9.webp");
                Product product9 = new Product("Trench Encerrado Cuello Combinado",
                        "Trench regular fit confeccionado en tejido de algodón con acabado encerado.", 89.95,
                        imageBytes9, 10, CategoryType.ABRIGOS);
                productRepository.save(product9);

                Blob imageBytes10 = loadImage("images/abrigos/abrigo10.webp");
                Product product10 = new Product("Gabardina Relaxed Fit",
                        "Gabardina relaxed fit confeccionada en tejido técnico.", 79.95, imageBytes10, 0,
                        CategoryType.ABRIGOS);
                productRepository.save(product10);

                Blob imageBytes11 = loadImage("images/abrigos/abrigo11.webp");
                Product product11 = new Product("Abrigo Cruzado Mezcla Lana",
                        "Abrigo entallado confeccionado en hilatura con mezcla de lana.", 129.00, imageBytes11, 10,
                        CategoryType.ABRIGOS);
                productRepository.save(product11);

                Blob imageBytes12 = loadImage("images/abrigos/abrigo12.webp");
                Product product12 = new Product("Abrigo Oversize Soft",
                        "Abrigo de cuello solapa y manga larga acabada con trabilla y botón.", 79.95, imageBytes12, 10,
                        CategoryType.ABRIGOS);
                productRepository.save(product12);

                // CAMISETAS
                Blob imageBytes13 = loadImage("images/camisetas/camiseta1.webp");
                Product product13 = new Product("Oversize Soft",
                        "Cuello solapa y manga larga acabada con trabilla y botón.", 79.95, imageBytes13, 10,
                        CategoryType.CAMISETAS);
                productRepository.save(product13);

                Blob imageBytes14 = loadImage("images/camisetas/camiseta2.webp");
                Product product14 = new Product("Basic Fit", "Camiseta de algodón con corte ajustado.", 19.99,
                        imageBytes14, 10, CategoryType.CAMISETAS);
                productRepository.save(product14);

                Blob imageBytes15 = loadImage("images/camisetas/camiseta3.webp");
                Product product15 = new Product("Vintage Stripes", "Camiseta con diseño a rayas y ajuste regular.",
                        24.99, imageBytes15, 10, CategoryType.CAMISETAS);
                productRepository.save(product15);

                Blob imageBytes16 = loadImage("images/camisetas/camiseta4.webp");
                Product product16 = new Product("Urban Style", "Camiseta oversized con estampado urbano.", 29.99,
                        imageBytes16, 10, CategoryType.CAMISETAS);
                productRepository.save(product16);

                Blob imageBytes17 = loadImage("images/camisetas/camiseta5.webp");
                Product product17 = new Product("Essential Black", "Camiseta negra de algodón premium.", 21.99,
                        imageBytes17, 10, CategoryType.CAMISETAS);
                productRepository.save(product17);

                Blob imageBytes18 = loadImage("images/camisetas/camiseta6.webp");
                Product product18 = new Product("Soft Touch", "Camiseta de tacto suave y cuello redondo.", 18.99,
                        imageBytes18, 10, CategoryType.CAMISETAS);
                productRepository.save(product18);

                Blob imageBytes19 = loadImage("images/camisetas/camiseta7.webp");
                Product product19 = new Product("Graphic Tee", "Camiseta con estampado gráfico exclusivo.", 27.99,
                        imageBytes19, 10, CategoryType.CAMISETAS);
                productRepository.save(product19);

                Blob imageBytes20 = loadImage("images/camisetas/camiseta8.webp");
                Product product20 = new Product("Loose Fit", "Camiseta de corte suelto, ideal para verano.", 22.99,
                        imageBytes20, 10, CategoryType.CAMISETAS);
                productRepository.save(product20);

                Blob imageBytes21 = loadImage("images/camisetas/camiseta9.webp");
                Product product21 = new Product("Denim Blue", "Camiseta azul denim con detalles en costuras.", 26.99,
                        imageBytes21, 10, CategoryType.CAMISETAS);
                productRepository.save(product21);

                Blob imageBytes22 = loadImage("images/camisetas/camiseta10.webp");
                Product product22 = new Product("Minimalist", "Camiseta minimalista de algodón orgánico.", 23.99,
                        imageBytes22, 10, CategoryType.CAMISETAS);
                productRepository.save(product22);

                Blob imageBytes23 = loadImage("images/camisetas/camiseta11.webp");
                Product product23 = new Product("Classic White", "Camiseta blanca clásica, imprescindible.", 19.99,
                        imageBytes23, 10, CategoryType.CAMISETAS);
                productRepository.save(product23);

                Blob imageBytes24 = loadImage("images/camisetas/camiseta12.webp");
                Product product24 = new Product("Retro Vibes", "Camiseta con estampado retro colorido.", 28.99,
                        imageBytes24, 10, CategoryType.CAMISETAS);
                productRepository.save(product24);

                // PANTALONES
                Blob imageBytes25 = loadImage("images/pantalones/pantalon1.webp");
                Product product25 = new Product("Retro Vibes", "Pantalón con estampado retro colorido.", 28.99,
                        imageBytes25, 10, CategoryType.PANTALONES);
                productRepository.save(product25);

                Blob imageBytes26 = loadImage("images/pantalones/pantalon2.webp");
                Product product26 = new Product("Skinny Jeans", "Pantalón vaquero ajustado con estilo moderno.", 39.99,
                        imageBytes26, 10, CategoryType.PANTALONES);
                productRepository.save(product26);

                Blob imageBytes27 = loadImage("images/pantalones/pantalon3.webp");
                Product product27 = new Product("Cargo Pants", "Pantalón cargo con múltiples bolsillos.", 45.99,
                        imageBytes27, 10, CategoryType.PANTALONES);
                productRepository.save(product27);

                Blob imageBytes28 = loadImage("images/pantalones/pantalon4.webp");
                Product product28 = new Product("Chino Slim", "Pantalón chino de corte slim y tela ligera.", 42.99,
                        imageBytes28, 10, CategoryType.PANTALONES);
                productRepository.save(product28);

                Blob imageBytes29 = loadImage("images/pantalones/pantalon5.webp");
                Product product29 = new Product("Jogger Fit", "Pantalón jogger cómodo para uso diario.", 34.99,
                        imageBytes29, 10, CategoryType.PANTALONES);
                productRepository.save(product29);

                Blob imageBytes30 = loadImage("images/pantalones/pantalon6.webp");
                Product product30 = new Product("Classic Denim", "Pantalón vaquero clásico con ajuste recto.", 49.99,
                        imageBytes30, 10, CategoryType.PANTALONES);
                productRepository.save(product30);

                Blob imageBytes31 = loadImage("images/pantalones/pantalon7.webp");
                Product product31 = new Product("Wide Leg", "Pantalón de pierna ancha con estilo retro.", 46.99,
                        imageBytes31, 10, CategoryType.PANTALONES);
                productRepository.save(product31);

                Blob imageBytes32 = loadImage("images/pantalones/pantalon8.webp");
                Product product32 = new Product("Linen Pants", "Pantalón de lino ligero, ideal para verano.", 38.99,
                        imageBytes32, 10, CategoryType.PANTALONES);
                productRepository.save(product32);

                Blob imageBytes33 = loadImage("images/pantalones/pantalon9.webp");
                Product product33 = new Product("Tailored Fit", "Pantalón de vestir con corte entallado.", 55.99,
                        imageBytes33, 10, CategoryType.PANTALONES);
                productRepository.save(product33);

                Blob imageBytes34 = loadImage("images/pantalones/pantalon10.webp");
                Product product34 = new Product("Baggy Jeans", "Pantalón vaquero ancho de estilo noventero.", 44.99,
                        imageBytes34, 10, CategoryType.PANTALONES);
                productRepository.save(product34);

                Blob imageBytes35 = loadImage("images/pantalones/pantalon11.webp");
                Product product35 = new Product("Straight Cut", "Pantalón recto con diseño clásico.", 41.99,
                        imageBytes35, 10, CategoryType.PANTALONES);
                productRepository.save(product35);

                Blob imageBytes36 = loadImage("images/pantalones/pantalon12.webp");
                Product product36 = new Product("Relaxed Fit", "Pantalón de corte relajado para máxima comodidad.",
                        37.99, imageBytes36, 10, CategoryType.PANTALONES);
                productRepository.save(product36);

                // JERSÉIS
                Blob imageBytes37 = loadImage("images/jerséis/jersey1.webp");
                Product product37 = new Product("Relaxed Fit", "Corte relajado para máxima comodidad.", 37.99,
                        imageBytes37, 10, CategoryType.JERSEYS);
                productRepository.save(product37);

                Blob imageBytes38 = loadImage("images/jerséis/jersey2.webp");
                Product product38 = new Product("Classic Knit", "Jersey de punto con diseño clásico y elegante.", 42.99,
                        imageBytes38, 10, CategoryType.JERSEYS);
                productRepository.save(product38);

                Blob imageBytes39 = loadImage("images/jerséis/jersey3.webp");
                Product product39 = new Product("Turtleneck Sweater",
                        "Jersey de cuello alto para un estilo sofisticado.", 49.99, imageBytes39, 10,
                        CategoryType.JERSEYS);
                productRepository.save(product39);

                Blob imageBytes40 = loadImage("images/jerséis/jersey4.webp");
                Product product40 = new Product("Chunky Knit", "Jersey grueso de punto con textura cálida.", 55.99,
                        imageBytes40, 10, CategoryType.JERSEYS);
                productRepository.save(product40);

                Blob imageBytes41 = loadImage("images/jerséis/jersey5.webp");
                Product product41 = new Product("Cable Knit", "Jersey con diseño trenzado en el tejido.", 47.99,
                        imageBytes41, 10, CategoryType.JERSEYS);
                productRepository.save(product41);

                Blob imageBytes42 = loadImage("images/jerséis/jersey6.webp");
                Product product42 = new Product("Slim Fit Sweater", "Jersey ajustado con diseño moderno.", 39.99,
                        imageBytes42, 10, CategoryType.JERSEYS);
                productRepository.save(product42);

                Blob imageBytes43 = loadImage("images/jerséis/jersey7.webp");
                Product product43 = new Product("Oversized Knit", "Jersey oversized para un look relajado.", 50.99,
                        imageBytes43, 10, CategoryType.JERSEYS);
                productRepository.save(product43);

                Blob imageBytes44 = loadImage("images/jerséis/jersey8.webp");
                Product product44 = new Product("Striped Sweater", "Jersey a rayas con diseño casual.", 44.99,
                        imageBytes44, 10, CategoryType.JERSEYS);
                productRepository.save(product44);

                Blob imageBytes45 = loadImage("images/jerséis/jersey9.webp");
                Product product45 = new Product("Wool Blend", "Jersey de lana mezclada para mayor calidez.", 59.99,
                        imageBytes45, 10, CategoryType.JERSEYS);
                productRepository.save(product45);

                Blob imageBytes46 = loadImage("images/jerséis/jersey10.webp");
                Product product46 = new Product("Mock Neck", "Jersey con cuello semi-alto y diseño minimalista.", 41.99,
                        imageBytes46, 10, CategoryType.JERSEYS);
                productRepository.save(product46);

                Blob imageBytes47 = loadImage("images/jerséis/jersey11.webp");
                Product product47 = new Product("V-Neck Sweater", "Jersey de cuello en V, ideal para capas.", 43.99,
                        imageBytes47, 10, CategoryType.JERSEYS);
                productRepository.save(product47);

                Blob imageBytes48 = loadImage("images/jerséis/jersey12.webp");
                Product product48 = new Product("Cozy Fleece", "Jersey de felpa suave para máxima comodidad.", 38.99,
                        imageBytes48, 10, CategoryType.JERSEYS);
                productRepository.save(product48);

                List<Product> savedProducts = productRepository.findAll();

                // Inicializar tallas para cada producto
                List<Size> sizes = new ArrayList<>();
                Random random = new Random();

                for (Product product : savedProducts) {
                    // Crear las tallas con stock
                    Size sizeS = new Size(Size.SizeName.S, random.nextInt(15) + 5, product);
                    Size sizeM = new Size(Size.SizeName.M, random.nextInt(15) + 5, product);
                    Size sizeL = new Size(Size.SizeName.L, random.nextInt(15) + 5, product);
                    Size sizeXL = new Size(Size.SizeName.XL, random.nextInt(15) + 5, product);

                    // Sumar el stock de cada talla al producto
                    product.setStock(product.getStock() + sizeS.getStock());
                    product.setStock(product.getStock() + sizeM.getStock());
                    product.setStock(product.getStock() + sizeL.getStock());
                    product.setStock(product.getStock() + sizeXL.getStock());

                    // Agregar las tallas a la lista
                    sizes.add(sizeS);
                    sizes.add(sizeM);
                    sizes.add(sizeL);
                    sizes.add(sizeXL);

                    // Guardar el producto actualizado con el stock global
                    productRepository.save(product);
                }

                sizeRepository.saveAll(sizes);

                logger.info("Productos y tallas cargados correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar productos y tallas", e);
            }
        } else {
            logger.info("Los productos o las tallas ya estaban en la base de datos. No se insertaron nuevos.");
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            logger.info("Cargando usuarios...");
            try {
                // Admin
                User admin = new User("Laura", "Moreno", "laura1@gmail.com", passwordEncoder.encode("Laura.53"), "USER",
                        "ADMIN");
                userRepository.save(admin);

                Blob imageBytes49 = loadImage("images/users/perfil1.webp");
                User user1 = new User("Paco", "García", imageBytes49, "Calle Mirador 12-C", "pacoG@gmail.com",
                        passwordEncoder.encode("12345"),
                        "USER");
                userRepository.save(user1);

                Blob imageBytes50 = loadImage("images/users/perfil2.webp");
                User user2 = new User("Ana", "López", imageBytes50, "Avenida de la Paz 4-B", "anaL@gmail.com",
                        passwordEncoder.encode("54321"),
                        "USER");
                userRepository.save(user2);

                Blob imageBytes51 = loadImage("images/users/perfil3.webp");
                User user3 = new User("Carlos", "Martínez", imageBytes51, "Calle Gran Vía 3-A", "carlosM@gmail.com",
                        passwordEncoder.encode("67890"),
                        "USER");
                userRepository.save(user3);

                Blob imageBytes52 = loadImage("images/users/perfil4.webp");
                User user4 = new User("María", "Fernández", imageBytes52, "Calle Toledo 15", "mariaF@gmail.com",
                        passwordEncoder.encode("11223"),
                        "USER");
                userRepository.save(user4);

                Blob imageBytes53 = loadImage("images/users/perfil5.webp");
                User user5 = new User("Luis", "Sánchez", imageBytes53, "Plaza Mayor 7", "luisS@gmail.com",
                        passwordEncoder.encode("44556"),
                        "USER");
                userRepository.save(user5);

                Blob imageBytes54 = loadImage("images/users/perfil6.webp");
                User user6 = new User("José", "Pérez", imageBytes54, "Calle de la Luna 9", "joseP@gmail.com",
                        passwordEncoder.encode("78901"),
                        "USER");
                userRepository.save(user6);

                Blob imageBytes55 = loadImage("images/users/perfil7.webp");
                User user7 = new User("Laura", "Rodríguez", imageBytes55, "Calle San Juan 10", "lauraR@gmail.com",
                        passwordEncoder.encode("23456"),
                        "USER");
                userRepository.save(user7);

                Blob imageBytes56 = loadImage("images/users/perfil8.webp");
                User user8 = new User("David", "González", imageBytes56, "Avenida de Andalucía 14", "davidG@gmail.com",
                        passwordEncoder.encode("67890"),
                        "USER");
                userRepository.save(user8);

                Blob imageBytes57 = loadImage("images/users/perfil9.webp");
                User user9 = new User("Elena", "Díaz", imageBytes57, "Calle del Sol 21", "elenaD@gmail.com",
                        passwordEncoder.encode("11234"),
                        "USER");
                userRepository.save(user9);

                Blob imageBytes58 = loadImage("images/users/perfil10.webp");
                User user10 = new User("Sergio", "Jiménez", imageBytes58, "Calle del Mar 18", "sergioJ@gmail.com",
                        passwordEncoder.encode("66778"),
                        "USER");
                userRepository.save(user10);

                logger.info("Usuarios cargados correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar usuarios", e);
            }
        } else {
            logger.info("Los usuarios ya estaban en la base de datos.");
        }
    }

    private void initializeReviews() {
        if (reviewRepository.count() == 0) {
            try {
                logger.info("Cargando reseñas...");

                Optional<Product> product1 = productRepository.findById(1L);
                Optional<Product> product2 = productRepository.findById(2L);
                Optional<Product> product3 = productRepository.findById(3L);
                Optional<Product> product4 = productRepository.findById(4L);
                Optional<Product> product5 = productRepository.findById(5L);
                Optional<Product> product6 = productRepository.findById(6L);
                Optional<Product> product7 = productRepository.findById(7L);
                Optional<Product> product8 = productRepository.findById(8L);
                Optional<Product> product9 = productRepository.findById(9L);
                Optional<Product> product10 = productRepository.findById(10L);
                Optional<Product> product11 = productRepository.findById(11L);
                Optional<Product> product12 = productRepository.findById(12L);
                Optional<Product> product13 = productRepository.findById(13L);
                Optional<Product> product14 = productRepository.findById(14L);
                Optional<Product> product15 = productRepository.findById(15L);
                Optional<Product> product16 = productRepository.findById(16L);
                Optional<Product> product17 = productRepository.findById(17L);
                Optional<Product> product18 = productRepository.findById(18L);
                Optional<Product> product19 = productRepository.findById(19L);
                Optional<Product> product20 = productRepository.findById(20L);
                Optional<Product> product21 = productRepository.findById(21L);
                Optional<Product> product22 = productRepository.findById(22L);
                Optional<Product> product23 = productRepository.findById(23L);
                Optional<Product> product24 = productRepository.findById(24L);
                Optional<Product> product25 = productRepository.findById(25L);
                Optional<Product> product26 = productRepository.findById(26L);
                Optional<Product> product27 = productRepository.findById(27L);
                Optional<Product> product28 = productRepository.findById(28L);
                Optional<Product> product29 = productRepository.findById(29L);
                Optional<Product> product30 = productRepository.findById(30L);
                Optional<Product> product31 = productRepository.findById(31L);
                Optional<Product> product32 = productRepository.findById(32L);
                Optional<Product> product33 = productRepository.findById(33L);
                Optional<Product> product34 = productRepository.findById(34L);
                Optional<Product> product35 = productRepository.findById(35L);
                Optional<Product> product36 = productRepository.findById(36L);
                Optional<Product> product37 = productRepository.findById(37L);
                Optional<Product> product38 = productRepository.findById(38L);
                Optional<Product> product39 = productRepository.findById(39L);
                Optional<Product> product40 = productRepository.findById(40L);
                Optional<Product> product41 = productRepository.findById(41L);
                Optional<Product> product42 = productRepository.findById(42L);
                Optional<Product> product43 = productRepository.findById(43L);
                Optional<Product> product44 = productRepository.findById(44L);
                Optional<Product> product45 = productRepository.findById(45L);
                Optional<Product> product46 = productRepository.findById(46L);
                Optional<Product> product47 = productRepository.findById(47L);
                Optional<Product> product48 = productRepository.findById(48L);

                Optional<User> user1 = userRepository.findById(1L);
                Optional<User> user2 = userRepository.findById(2L);
                Optional<User> user3 = userRepository.findById(3L);
                Optional<User> user4 = userRepository.findById(4L);
                Optional<User> user5 = userRepository.findById(5L);
                Optional<User> user6 = userRepository.findById(6L);
                Optional<User> user7 = userRepository.findById(7L);
                Optional<User> user8 = userRepository.findById(8L);
                Optional<User> user9 = userRepository.findById(9L);
                Optional<User> user10 = userRepository.findById(10L);

                if (product1.isPresent()) {
                    if (user1.isPresent()) {
                        Review review1 = new Review(5, "Excelente producto", false, product1.get(), user1.get());
                        reviewRepository.save(review1);
                    }
                    if (user2.isPresent()) {
                        Review review2 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review2);
                        Review review3 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review3);
                        Review review4 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review4);
                        Review review5 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review5);
                        Review review6 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review6);
                        Review review7 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review7);
                        Review review8 = new Review(3, "Buena calidad, pero un poco caro", false, product1.get(),
                                user2.get());
                        reviewRepository.save(review8);
                    }
                }

                if (product2.isPresent()) {
                    if (user2.isPresent()) {
                        Review review1 = new Review(4, "Producto muy bueno", false, product2.get(), user2.get());
                        reviewRepository.save(review1);
                    }
                    if (user3.isPresent()) {
                        Review review2 = new Review(2, "La calidad no es lo que esperaba", false, product2.get(),
                                user3.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product3.isPresent()) {
                    if (user3.isPresent()) {
                        Review review1 = new Review(5, "Me encanta, superó mis expectativas", false, product3.get(),
                                user3.get());
                        reviewRepository.save(review1);
                    }
                    if (user4.isPresent()) {
                        Review review2 = new Review(1, "No me gustó para nada", false, product3.get(), user4.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product4.isPresent()) {
                    if (user4.isPresent()) {
                        Review review1 = new Review(3, "Buen producto, pero mejorable", false, product4.get(),
                                user4.get());
                        reviewRepository.save(review1);
                    }
                    if (user5.isPresent()) {
                        Review review2 = new Review(5, "Totalmente recomendado", false, product4.get(), user5.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product5.isPresent()) {
                    if (user5.isPresent()) {
                        Review review1 = new Review(4, "Lo usaré más veces, buena compra", false, product5.get(),
                                user5.get());
                        reviewRepository.save(review1);
                    }
                    if (user6.isPresent()) {
                        Review review2 = new Review(2, "No cumple lo prometido", false, product5.get(), user6.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product6.isPresent()) {
                    if (user6.isPresent()) {
                        Review review1 = new Review(4, "Calidad buena, el envío tardó", false, product6.get(),
                                user6.get());
                        reviewRepository.save(review1);
                    }
                    if (user7.isPresent()) {
                        Review review2 = new Review(3, "Bien, pero esperaba más", false, product6.get(), user7.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product7.isPresent()) {
                    if (user7.isPresent()) {
                        Review review1 = new Review(5, "Excelente calidad y muy útil", false, product7.get(),
                                user7.get());
                        reviewRepository.save(review1);
                    }
                    if (user8.isPresent()) {
                        Review review2 = new Review(1, "No me sirvió para nada", false, product7.get(), user8.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product8.isPresent()) {
                    if (user8.isPresent()) {
                        Review review1 = new Review(3, "Está bien para el precio", false, product8.get(), user8.get());
                        reviewRepository.save(review1);
                    }
                    if (user9.isPresent()) {
                        Review review2 = new Review(5, "Muy práctico, lo recomiendo", false, product8.get(),
                                user9.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product9.isPresent()) {
                    if (user9.isPresent()) {
                        Review review1 = new Review(4, "Me gustó mucho el diseño", false, product9.get(), user9.get());
                        reviewRepository.save(review1);
                    }
                    if (user10.isPresent()) {
                        Review review2 = new Review(3, "El producto llegó dañado", false, product9.get(), user10.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product10.isPresent()) {
                    if (user10.isPresent()) {
                        Review review1 = new Review(5, "Totalmente satisfecho con la compra", false, product10.get(),
                                user10.get());
                        reviewRepository.save(review1);
                    }
                    if (user1.isPresent()) {
                        Review review2 = new Review(2, "No se ajusta a lo que esperaba", false, product10.get(),
                                user1.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product11.isPresent()) {
                    if (user1.isPresent()) {
                        Review review1 = new Review(4, "Muy buen producto, cumple lo prometido", false, product11.get(),
                                user1.get());
                        reviewRepository.save(review1);
                    }
                    if (user2.isPresent()) {
                        Review review2 = new Review(3, "Buena calidad, pero se tarda en llegar", false, product11.get(),
                                user2.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product12.isPresent()) {
                    if (user2.isPresent()) {
                        Review review1 = new Review(5, "Me ha encantado, vale la pena", false, product12.get(),
                                user2.get());
                        reviewRepository.save(review1);
                    }
                    if (user3.isPresent()) {
                        Review review2 = new Review(2, "No lo recomendaría, esperaba más", false, product12.get(),
                                user3.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product13.isPresent()) {
                    if (user3.isPresent()) {
                        Review review1 = new Review(5, "Perfecto para mis necesidades, excelente", false,
                                product13.get(), user3.get());
                        reviewRepository.save(review1);
                    }
                    if (user4.isPresent()) {
                        Review review2 = new Review(1, "No cumple con la descripción", false, product13.get(),
                                user4.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product14.isPresent()) {
                    if (user4.isPresent()) {
                        Review review1 = new Review(4, "Muy buena opción, pero con detalles a mejorar", false,
                                product14.get(), user4.get());
                        reviewRepository.save(review1);
                    }
                    if (user5.isPresent()) {
                        Review review2 = new Review(5, "Gran compra, muy contento", false, product14.get(),
                                user5.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product15.isPresent()) {
                    if (user5.isPresent()) {
                        Review review1 = new Review(3, "El producto es bueno, pero el envío fue lento", false,
                                product15.get(), user5.get());
                        reviewRepository.save(review1);
                    }
                    if (user6.isPresent()) {
                        Review review2 = new Review(2, "No me convenció del todo", false, product15.get(), user6.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product16.isPresent()) {
                    if (user6.isPresent()) {
                        Review review1 = new Review(4, "Producto útil y fácil de usar", false, product16.get(),
                                user6.get());
                        reviewRepository.save(review1);
                    }
                    if (user7.isPresent()) {
                        Review review2 = new Review(3, "Buena calidad, pero algo caro", false, product16.get(),
                                user7.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product17.isPresent()) {
                    if (user7.isPresent()) {
                        Review review1 = new Review(5, "Maravilloso, completamente satisfecho", false, product17.get(),
                                user7.get());
                        reviewRepository.save(review1);
                    }
                    if (user8.isPresent()) {
                        Review review2 = new Review(2, "No era lo que esperaba", false, product17.get(), user8.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product18.isPresent()) {
                    if (user8.isPresent()) {
                        Review review1 = new Review(4, "Buen producto, lo recomiendo", false, product18.get(),
                                user8.get());
                        reviewRepository.save(review1);
                    }
                    if (user9.isPresent()) {
                        Review review2 = new Review(1, "No me funcionó correctamente", false, product18.get(),
                                user9.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product19.isPresent()) {
                    if (user9.isPresent()) {
                        Review review1 = new Review(3, "Está bien para lo que cuesta", false, product19.get(),
                                user9.get());
                        reviewRepository.save(review1);
                    }
                    if (user10.isPresent()) {
                        Review review2 = new Review(5, "Excelente, lo volveré a comprar", false, product19.get(),
                                user10.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product20.isPresent()) {
                    if (user10.isPresent()) {
                        Review review1 = new Review(4, "Buen desempeño, aunque le falta algo", false, product20.get(),
                                user10.get());
                        reviewRepository.save(review1);
                    }
                    if (user1.isPresent()) {
                        Review review2 = new Review(3, "Es bueno, pero no lo que esperaba", false, product20.get(),
                                user1.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product21.isPresent()) {
                    if (user1.isPresent()) {
                        Review review1 = new Review(4, "Buena calidad, aunque algo pesado", false, product21.get(),
                                user1.get());
                        reviewRepository.save(review1);
                    }
                    if (user2.isPresent()) {
                        Review review2 = new Review(5, "Muy práctico y funcional", false, product21.get(), user2.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product22.isPresent()) {
                    if (user2.isPresent()) {
                        Review review1 = new Review(3, "Cumple su función, pero no tiene muchas características", false,
                                product22.get(), user2.get());
                        reviewRepository.save(review1);
                    }
                    if (user3.isPresent()) {
                        Review review2 = new Review(2, "No es lo que esperaba, le faltan detalles", false,
                                product22.get(), user3.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product23.isPresent()) {
                    if (user3.isPresent()) {
                        Review review1 = new Review(5, "Lo mejor que he comprado, estoy muy contento", false,
                                product23.get(), user3.get());
                        reviewRepository.save(review1);
                    }
                    if (user4.isPresent()) {
                        Review review2 = new Review(4, "Buena compra, aunque mejoraría el diseño", false,
                                product23.get(), user4.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product24.isPresent()) {
                    if (user4.isPresent()) {
                        Review review1 = new Review(5, "Excelente calidad y precio", false, product24.get(),
                                user4.get());
                        reviewRepository.save(review1);
                    }
                    if (user5.isPresent()) {
                        Review review2 = new Review(4, "Muy bueno, pero el servicio de entrega fue lento", false,
                                product24.get(), user5.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product25.isPresent()) {
                    if (user5.isPresent()) {
                        Review review1 = new Review(3, "Buena calidad, pero no duró tanto como esperaba", false,
                                product25.get(), user5.get());
                        reviewRepository.save(review1);
                    }
                    if (user6.isPresent()) {
                        Review review2 = new Review(4, "Muy funcional, aunque el tamaño es un poco grande", false,
                                product25.get(), user6.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product26.isPresent()) {
                    if (user6.isPresent()) {
                        Review review1 = new Review(5, "Es justo lo que buscaba, lo recomiendo mucho", false,
                                product26.get(), user6.get());
                        reviewRepository.save(review1);
                    }
                    if (user7.isPresent()) {
                        Review review2 = new Review(2, "No es tan útil como pensaba", false, product26.get(),
                                user7.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product27.isPresent()) {
                    if (user7.isPresent()) {
                        Review review1 = new Review(4, "Buen producto, pero el precio podría ser más bajo", false,
                                product27.get(), user7.get());
                        reviewRepository.save(review1);
                    }
                    if (user8.isPresent()) {
                        Review review2 = new Review(5, "Perfecto para mi uso diario, no me arrepiento", false,
                                product27.get(), user8.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product28.isPresent()) {
                    if (user8.isPresent()) {
                        Review review1 = new Review(3, "Buen producto, pero no es tan duradero como esperábamos", false,
                                product28.get(), user8.get());
                        reviewRepository.save(review1);
                    }
                    if (user9.isPresent()) {
                        Review review2 = new Review(4, "Buen rendimiento, pero el diseño podría mejorar", false,
                                product28.get(), user9.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product29.isPresent()) {
                    if (user9.isPresent()) {
                        Review review1 = new Review(5, "Superó mis expectativas, muy recomendado", false,
                                product29.get(), user9.get());
                        reviewRepository.save(review1);
                    }
                    if (user10.isPresent()) {
                        Review review2 = new Review(4, "Es bueno, aunque me gustaría que fuera más compacto", false,
                                product29.get(), user10.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product30.isPresent()) {
                    if (user10.isPresent()) {
                        Review review1 = new Review(2, "No funciona correctamente, muy decepcionado", false,
                                product30.get(), user10.get());
                        reviewRepository.save(review1);
                    }
                    if (user1.isPresent()) {
                        Review review2 = new Review(4, "Está bien, aunque tiene algunos detalles que podrían mejorar",
                                false, product30.get(), user1.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product31.isPresent()) {
                    if (user1.isPresent()) {
                        Review review1 = new Review(5, "Producto increíble, definitivamente lo recomendaré", false,
                                product31.get(), user1.get());
                        reviewRepository.save(review1);
                    }
                    if (user2.isPresent()) {
                        Review review2 = new Review(4, "Es bastante bueno, aunque algo pesado", false, product31.get(),
                                user2.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product32.isPresent()) {
                    if (user2.isPresent()) {
                        Review review1 = new Review(3, "Cumple su función, pero no tiene tantas características", false,
                                product32.get(), user2.get());
                        reviewRepository.save(review1);
                    }
                    if (user3.isPresent()) {
                        Review review2 = new Review(4, "Buen rendimiento, pero algo caro para lo que ofrece", false,
                                product32.get(), user3.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product33.isPresent()) {
                    if (user3.isPresent()) {
                        Review review1 = new Review(5, "Excelente, ya lo tengo en mi lista de favoritos", false,
                                product33.get(), user3.get());
                        reviewRepository.save(review1);
                    }
                    if (user4.isPresent()) {
                        Review review2 = new Review(4, "Me gusta mucho, aunque la entrega fue algo demorada", false,
                                product33.get(), user4.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product34.isPresent()) {
                    if (user4.isPresent()) {
                        Review review1 = new Review(5, "Perfecto, cumple todo lo que prometen", false, product34.get(),
                                user4.get());
                        reviewRepository.save(review1);
                    }
                    if (user5.isPresent()) {
                        Review review2 = new Review(3, "Es bueno, pero esperaba más por el precio", false,
                                product34.get(), user5.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product35.isPresent()) {
                    if (user5.isPresent()) {
                        Review review1 = new Review(4, "Buen producto, pero el diseño podría mejorarse", false,
                                product35.get(), user5.get());
                        reviewRepository.save(review1);
                    }
                    if (user6.isPresent()) {
                        Review review2 = new Review(5, "Lo uso todos los días, muy funcional", false, product35.get(),
                                user6.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product36.isPresent()) {
                    if (user6.isPresent()) {
                        Review review1 = new Review(3, "Está bien, pero me gustaría que tuviera más opciones", false,
                                product36.get(), user6.get());
                        reviewRepository.save(review1);
                    }
                    if (user7.isPresent()) {
                        Review review2 = new Review(4, "Satisfactorio, pero el manual de instrucciones es confuso",
                                false, product36.get(), user7.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product37.isPresent()) {
                    if (user7.isPresent()) {
                        Review review1 = new Review(5, "El mejor producto que he comprado en mucho tiempo", false,
                                product37.get(), user7.get());
                        reviewRepository.save(review1);
                    }
                    if (user8.isPresent()) {
                        Review review2 = new Review(4, "Me encanta, pero el precio es algo alto", false,
                                product37.get(), user8.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product38.isPresent()) {
                    if (user8.isPresent()) {
                        Review review1 = new Review(2, "No cumplió con mis expectativas, me decepcionó", false,
                                product38.get(), user8.get());
                        reviewRepository.save(review1);
                    }
                    if (user9.isPresent()) {
                        Review review2 = new Review(3, "Apenas cumple su función, esperaba más", false, product38.get(),
                                user9.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product39.isPresent()) {
                    if (user9.isPresent()) {
                        Review review1 = new Review(5, "Muy satisfecho con la compra, todo excelente", false,
                                product39.get(), user9.get());
                        reviewRepository.save(review1);
                    }
                    if (user10.isPresent()) {
                        Review review2 = new Review(4, "Me gusta, pero el tamaño es un poco grande para mí", false,
                                product39.get(), user10.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product40.isPresent()) {
                    if (user10.isPresent()) {
                        Review review1 = new Review(3, "No es malo, pero se puede mejorar", false, product40.get(),
                                user10.get());
                        reviewRepository.save(review1);
                    }
                    if (user1.isPresent()) {
                        Review review2 = new Review(4, "Es un buen producto, aunque la entrega fue algo tardada", false,
                                product40.get(), user1.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product41.isPresent()) {
                    if (user1.isPresent()) {
                        Review review1 = new Review(5, "Excelente calidad y muy funcional, lo uso todos los días",
                                false, product41.get(), user1.get());
                        reviewRepository.save(review1);
                    }
                    if (user2.isPresent()) {
                        Review review2 = new Review(4, "Buen producto, pero el diseño podría ser más moderno", false,
                                product41.get(), user2.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product42.isPresent()) {
                    if (user2.isPresent()) {
                        Review review1 = new Review(3, "Funciona bien, pero algo lento en comparación con otros", false,
                                product42.get(), user2.get());
                        reviewRepository.save(review1);
                    }
                    if (user3.isPresent()) {
                        Review review2 = new Review(5, "Me encantó, es muy rápido y fácil de usar", false,
                                product42.get(), user3.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product43.isPresent()) {
                    if (user3.isPresent()) {
                        Review review1 = new Review(4, "Buen producto, pero la durabilidad podría mejorar", false,
                                product43.get(), user3.get());
                        reviewRepository.save(review1);
                    }
                    if (user4.isPresent()) {
                        Review review2 = new Review(5, "Excelente relación calidad-precio, lo recomiendo", false,
                                product43.get(), user4.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product44.isPresent()) {
                    if (user4.isPresent()) {
                        Review review1 = new Review(3, "Está bien, pero esperaba más funciones por el precio", false,
                                product44.get(), user4.get());
                        reviewRepository.save(review1);
                    }
                    if (user5.isPresent()) {
                        Review review2 = new Review(4, "Buen producto, aunque la entrega se retrasó un poco", false,
                                product44.get(), user5.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product45.isPresent()) {
                    if (user5.isPresent()) {
                        Review review1 = new Review(5, "Muy satisfecho, superó mis expectativas", false,
                                product45.get(), user5.get());
                        reviewRepository.save(review1);
                    }
                    if (user6.isPresent()) {
                        Review review2 = new Review(4, "Es bueno, pero a veces tiene fallos en el rendimiento", false,
                                product45.get(), user6.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product46.isPresent()) {
                    if (user6.isPresent()) {
                        Review review1 = new Review(3, "El producto es funcional, pero no es tan potente como esperaba",
                                false, product46.get(), user6.get());
                        reviewRepository.save(review1);
                    }
                    if (user7.isPresent()) {
                        Review review2 = new Review(5, "Lo compré para mi oficina y ha sido una excelente decisión",
                                false, product46.get(), user7.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product47.isPresent()) {
                    if (user7.isPresent()) {
                        Review review1 = new Review(4, "Buen producto, pero el precio podría ser más bajo", false,
                                product47.get(), user7.get());
                        reviewRepository.save(review1);
                    }
                    if (user8.isPresent()) {
                        Review review2 = new Review(5, "Perfecto para lo que necesito, lo recomiendo sin dudas", false,
                                product47.get(), user8.get());
                        reviewRepository.save(review2);
                    }
                }

                if (product48.isPresent()) {
                    if (user8.isPresent()) {
                        Review review1 = new Review(2, "No cumplió con lo que esperaba, me decepcionó", false,
                                product48.get(), user8.get());
                        reviewRepository.save(review1);
                    }
                    if (user9.isPresent()) {
                        Review review2 = new Review(3, "Es adecuado, pero no destaca sobre otros productos similares",
                                false, product48.get(), user9.get());
                        reviewRepository.save(review2);
                    }
                }

                logger.info("Reseñas cargadas correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar reseñas", e);
            }
        } else {
            logger.info("Las reseñas ya estaban en la base de datos.");
        }
    }

    private void initializeOrders() {
        if (orderRepository.count() == 0) {
            try {
                logger.info("Cargando pedidos...");

                List<User> users = userRepository.findAll().stream()
                        .limit(10)
                        .collect(Collectors.toList());

                List<Product> products = productRepository.findAll();
                List<Size> sizes = sizeRepository.findAll();

                if (products.isEmpty() || sizes.isEmpty()) {
                    logger.warn("No hay productos o tallas en la base de datos, no se pueden crear pedidos.");
                    return;
                }

                Random random = new Random();

                // Order that is not paid
                if (users.get(1) != null && products.get(1) != null) {
                    Order order = new Order();
                    order.setUser(users.get(1));
                    order.setState(Order.State.No_pagado);
                    order.setIsPaid(false);

                    List<OrderProduct> orderProducts = new ArrayList<>();
                    int numProductos = random.nextInt(3) + 1;
                    Collections.shuffle(products);

                    for (int j = 0; j < numProductos; j++) {
                        Product product = products.get(j);
                        Size size = sizes.get(random.nextInt(sizes.size()));
                        int quantity = random.nextInt(3) + 1;

                        orderProducts.add(new OrderProduct(order, product, size.getName().toString(), quantity));
                    }

                    order.setOrderProducts(orderProducts);

                    orderRepository.save(order);
                    orderProductRepository.saveAll(orderProducts);
                }

                // Orders that are paid
                for (User user : users) {
                    int numPedidos = random.nextInt(3) + 1;
                    for (int i = 0; i < numPedidos; i++) {
                        Order order = new Order(user, State.Procesado, true);

                        List<OrderProduct> orderProducts = new ArrayList<>();
                        int numProductos = random.nextInt(3) + 1;
                        Collections.shuffle(products);

                        for (int j = 0; j < numProductos; j++) {
                            Product product = products.get(j);
                            Size size = sizes.get(random.nextInt(sizes.size()));
                            int quantity = random.nextInt(3) + 1;

                            orderProducts.add(new OrderProduct(order, product, size.getName().toString(), quantity));
                        }

                        order.setOrderProducts(orderProducts);

                        orderRepository.save(order);
                        orderProductRepository.saveAll(orderProducts);

                        for (OrderProduct orderProduct : orderProducts) {
                            Product product = orderProduct.getProduct();
                            product.incrementSold(orderProduct.getQuantity());
                            productRepository.save(product);
                        }
                    }
                }

                logger.info("Pedidos cargados correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar pedidos", e);
            }
        } else {
            logger.info("Los pedidos ya estaban en la base de datos. No se insertaron nuevos.");
        }
    }

    public Blob loadImage(String imagePath) {
        try {
            Resource resource = new ClassPathResource(imagePath);
            if (!resource.exists()) {
                System.out.println("Error: No se encontró la imagen en la ruta especificada.");
                return null;
            }
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] imageBytes = inputStream.readAllBytes();
                return new SerialBlob(imageBytes);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
