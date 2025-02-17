package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.repositories.ProductRepository;
import es.codeurjc.webapp14.repositories.ReviewRepository;
import es.codeurjc.webapp14.repositories.UserRepository;
import es.codeurjc.webapp14.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // Inyección de dependencias por constructor (Mejor práctica)
    public DataInitializer(ProductRepository productRepository, UserRepository userRepository,
            ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Inicializando datos...");
        initializeProducts();
        initializeUsers();
        initializeReviews();
        logger.info("Datos inicializados correctamente.");
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            try {
                logger.info("Cargando productos...");

                byte[] imageBytes1 = loadImage("src/main/resources/static/images/imagen7.webp");
                Product product1 = new Product("Camiseta Negra", "Camiseta de algodón negra", 19.99, imageBytes1);
                productRepository.save(product1);

                byte[] imageBytes2 = loadImage("src/main/resources/static/images/imagen8.webp");
                Product product2 = new Product("Camiseta Blanca", "Camiseta de algodón blanca", 19.99, imageBytes2);
                productRepository.save(product2);

                logger.info("Productos cargados correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar productos", e);
            }
        } else {
            logger.info("Los productos ya estaban en la base de datos. No se insertaron nuevos.");
        }
    }

    private void initializeUsers() {
        if (userRepository.findByRole(User.Role.ADMIN) == null) {
            logger.info("No hay administrador en la base de datos. Creando uno...");
            User admin = new User("Laura", "Gutierrez Navarro", "laura1.gn@gmail.com", "Laura.53", User.Role.ADMIN);
            userRepository.save(admin);
            logger.info("Administrador creado correctamente.");
        } else {
            logger.info("Ya existe un administrador en la base de datos.");
        }
    }
    
    private void initializeReviews() {
        if (reviewRepository.count() == 0) {
            logger.info("Cargando reseñas...");
            // Agregar lógica de creación de reseñas aquí
            logger.info("Reseñas cargadas correctamente.");
        } else {
            logger.info("Las reseñas ya estaban en la base de datos.");
        }
    }

    private byte[] loadImage(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (Exception e) {
            logger.error("No se pudo cargar la imagen: " + path, e);
            return new byte[0]; // Retorna una imagen vacía para evitar errores
        }
    }
}
