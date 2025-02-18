package es.codeurjc.webapp14.services;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Product.CategoryType;
import es.codeurjc.webapp14.repositories.ProductRepository;
import es.codeurjc.webapp14.repositories.ReviewRepository;
import es.codeurjc.webapp14.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Paths;
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

                //ABRIGOS
                byte[] imageBytes1 = loadImage("src/main/resources/images/abrigos/abrigo1.webp");
                Product product1 = new Product("Trench técnico", "Trench técnico con abrigo acolchado interior desmontable.", 79.95, imageBytes1,10, CategoryType.ABRIGOS);
                productRepository.save(product1);

                byte[] imageBytes2 = loadImage("src/main/resources/images/abrigos/abrigo2.webp");
                Product product2 = new Product("Trench técnico 2 en 1 ", "Trench técnico con abrigo acolchado interior desmontable.", 79.95, imageBytes2,0, CategoryType.ABRIGOS);
                productRepository.save(product2);

                byte[] imageBytes3 = loadImage("src/main/resources/images/abrigos/abrigo3.webp");
                Product product3 = new Product("Parka Desmonatble Water Repellent", "Parka relaxed fit confeccionada en tejido técnico que repele agua en contacto.", 89.95, imageBytes3,10, CategoryType.ABRIGOS);
                productRepository.save(product3);

                byte[] imageBytes4 = loadImage("src/main/resources/images/abrigos/abrigo4.webp");
                Product product4 = new Product("Abrigo con lana", "Abrigo straight fit confeccionado en tejido con lana. Cuello con solapas de muesca y manga larga acabada en puño con botones.", 69.95, imageBytes4,10, CategoryType.ABRIGOS);
                productRepository.save(product4);

                byte[] imageBytes5 = loadImage("src/main/resources/images/abrigos/abrigo5.webp");
                Product product5 = new Product("Anorak Water and Wind Protection", "Anorak resistente al agua y al viento con aislamiento térmico para climas fríos.", 69.95, imageBytes5,10, CategoryType.ABRIGOS);
                productRepository.save(product5);

                byte[] imageBytes6 = loadImage("src/main/resources/images/abrigos/abrigo6.webp");
                Product product6 = new Product("Trench cinturón", "Trench de cuello solapa y manga larga acabada con trabilla y botón.", 59.95, imageBytes6,10, CategoryType.ABRIGOS);
                productRepository.save(product6);

                byte[] imageBytes7 = loadImage("src/main/resources/images/abrigos/abrigo7.webp");
                Product product7 = new Product("Abrigo acolchado capucha", "Cazadora de cuello subido con capucha ajustable con cordones y manga larga.", 39.95, imageBytes7,10, CategoryType.ABRIGOS);
                productRepository.save(product7);

                byte[] imageBytes8 = loadImage("src/main/resources/images/abrigos/abrigo8.webp");
                Product product8 = new Product("Abrigo Soft", "Abrigo de cuello solapa y manga larga con hombreras. Bolsillos delanteros.", 39.95, imageBytes8,10, CategoryType.ABRIGOS);
                productRepository.save(product8);

                byte[] imageBytes9 = loadImage("src/main/resources/images/abrigos/abrigo9.webp");
                Product product9 = new Product("Trench Encerrado Cuello Combinado", "Trench regular fit confeccionado en tejido de algodón con acabado encerado.", 89.95, imageBytes9,10, CategoryType.ABRIGOS);
                productRepository.save(product9);

                byte[] imageBytes10 = loadImage("src/main/resources/images/abrigos/abrigo10.webp");
                Product product10 = new Product("Gabardina Relaxed Fit", "Gabardina relaxed fit confeccionada en tejido técnico.", 79.95, imageBytes10,10, CategoryType.ABRIGOS);
                productRepository.save(product10);

                byte[] imageBytes11 = loadImage("src/main/resources/images/abrigos/abrigo11.webp");
                Product product11 = new Product("Abrigo Cruzado Mezcla Lana", "Abrigo entallado confeccionado en hilatura con mezcla de lana.", 129.00, imageBytes11,10, CategoryType.ABRIGOS);
                productRepository.save(product11);

                byte[] imageBytes12 = loadImage("src/main/resources/images/abrigos/abrigo12.webp");
                Product product12 = new Product("Abrigo Oversize Soft", "Abrigo de cuello solapa y manga larga acabada con trabilla y botón.", 79.95, imageBytes12,10, CategoryType.ABRIGOS);
                productRepository.save(product12);


                //CAMISETAS
                byte[] imageBytes13 = loadImage("src/main/resources/images/camisetas/camiseta1.webp");
                Product product13 = new Product("Oversize Soft", "Cuello solapa y manga larga acabada con trabilla y botón.", 79.95, imageBytes13,10, CategoryType.CAMISETAS);
                productRepository.save(product13);

                byte[] imageBytes14 = loadImage("src/main/resources/images/camisetas/camiseta2.webp");
                Product product14 = new Product("Basic Fit", "Camiseta de algodón con corte ajustado.", 19.99, imageBytes14, 10, CategoryType.CAMISETAS);
                productRepository.save(product14);

                byte[] imageBytes15 = loadImage("src/main/resources/images/camisetas/camiseta3.webp");
                Product product15 = new Product("Vintage Stripes", "Camiseta con diseño a rayas y ajuste regular.", 24.99, imageBytes15, 10, CategoryType.CAMISETAS);
                productRepository.save(product15);

                byte[] imageBytes16 = loadImage("src/main/resources/images/camisetas/camiseta4.webp");
                Product product16 = new Product("Urban Style", "Camiseta oversized con estampado urbano.", 29.99, imageBytes16, 10, CategoryType.CAMISETAS);
                productRepository.save(product16);

                byte[] imageBytes17 = loadImage("src/main/resources/images/camisetas/camiseta5.webp");
                Product product17 = new Product("Essential Black", "Camiseta negra de algodón premium.", 21.99, imageBytes17, 10, CategoryType.CAMISETAS);
                productRepository.save(product17);

                byte[] imageBytes18 = loadImage("src/main/resources/images/camisetas/camiseta6.webp");
                Product product18 = new Product("Soft Touch", "Camiseta de tacto suave y cuello redondo.", 18.99, imageBytes18, 10, CategoryType.CAMISETAS);
                productRepository.save(product18);

                byte[] imageBytes19 = loadImage("src/main/resources/images/camisetas/camiseta7.webp");
                Product product19 = new Product("Graphic Tee", "Camiseta con estampado gráfico exclusivo.", 27.99, imageBytes19, 10, CategoryType.CAMISETAS);
                productRepository.save(product19);

                byte[] imageBytes20 = loadImage("src/main/resources/images/camisetas/camiseta8.webp");
                Product product20 = new Product("Loose Fit", "Camiseta de corte suelto, ideal para verano.", 22.99, imageBytes20, 10, CategoryType.CAMISETAS);
                productRepository.save(product20);

                byte[] imageBytes21 = loadImage("src/main/resources/images/camisetas/camiseta9.webp");
                Product product21 = new Product("Denim Blue", "Camiseta azul denim con detalles en costuras.", 26.99, imageBytes21, 10, CategoryType.CAMISETAS);
                productRepository.save(product21);

                byte[] imageBytes22 = loadImage("src/main/resources/images/camisetas/camiseta10.webp");
                Product product22 = new Product("Minimalist", "Camiseta minimalista de algodón orgánico.", 23.99, imageBytes22, 10, CategoryType.CAMISETAS);
                productRepository.save(product22);

                byte[] imageBytes23 = loadImage("src/main/resources/images/camisetas/camiseta11.webp");
                Product product23 = new Product("Classic White", "Camiseta blanca clásica, imprescindible.", 19.99, imageBytes23, 10, CategoryType.CAMISETAS);
                productRepository.save(product23);

                byte[] imageBytes24 = loadImage("src/main/resources/images/camisetas/camiseta12.webp");
                Product product24 = new Product("Retro Vibes", "Camiseta con estampado retro colorido.", 28.99, imageBytes24, 10, CategoryType.CAMISETAS);
                productRepository.save(product24);


                //PANTALONES
                byte[] imageBytes25 = loadImage("src/main/resources/images/pantalones/pantalon1.webp");
                Product product25 = new Product("Retro Vibes", "Pantalón con estampado retro colorido.", 28.99, imageBytes25, 10, CategoryType.PANTALONES);
                productRepository.save(product25);

                byte[] imageBytes26 = loadImage("src/main/resources/images/pantalones/pantalon2.webp");
                Product product26 = new Product("Skinny Jeans", "Pantalón vaquero ajustado con estilo moderno.", 39.99, imageBytes26, 10, CategoryType.PANTALONES);
                productRepository.save(product26);

                byte[] imageBytes27 = loadImage("src/main/resources/images/pantalones/pantalon3.webp");
                Product product27 = new Product("Cargo Pants", "Pantalón cargo con múltiples bolsillos.", 45.99, imageBytes27, 10, CategoryType.PANTALONES);
                productRepository.save(product27);

                byte[] imageBytes28 = loadImage("src/main/resources/images/pantalones/pantalon4.webp");
                Product product28 = new Product("Chino Slim", "Pantalón chino de corte slim y tela ligera.", 42.99, imageBytes28, 10, CategoryType.PANTALONES);
                productRepository.save(product28);

                byte[] imageBytes29 = loadImage("src/main/resources/images/pantalones/pantalon5.webp");
                Product product29 = new Product("Jogger Fit", "Pantalón jogger cómodo para uso diario.", 34.99, imageBytes29, 10, CategoryType.PANTALONES);
                productRepository.save(product29);

                byte[] imageBytes30 = loadImage("src/main/resources/images/pantalones/pantalon6.webp");
                Product product30 = new Product("Classic Denim", "Pantalón vaquero clásico con ajuste recto.", 49.99, imageBytes30, 10, CategoryType.PANTALONES);
                productRepository.save(product30);

                byte[] imageBytes31 = loadImage("src/main/resources/images/pantalones/pantalon7.webp");
                Product product31 = new Product("Wide Leg", "Pantalón de pierna ancha con estilo retro.", 46.99, imageBytes31, 10, CategoryType.PANTALONES);
                productRepository.save(product31);

                byte[] imageBytes32 = loadImage("src/main/resources/images/pantalones/pantalon8.webp");
                Product product32 = new Product("Linen Pants", "Pantalón de lino ligero, ideal para verano.", 38.99, imageBytes32, 10, CategoryType.PANTALONES);
                productRepository.save(product32);

                byte[] imageBytes33 = loadImage("src/main/resources/images/pantalones/pantalon9.webp");
                Product product33 = new Product("Tailored Fit", "Pantalón de vestir con corte entallado.", 55.99, imageBytes33, 10, CategoryType.PANTALONES);
                productRepository.save(product33);

                byte[] imageBytes34 = loadImage("src/main/resources/images/pantalones/pantalon10.webp");
                Product product34 = new Product("Baggy Jeans", "Pantalón vaquero ancho de estilo noventero.", 44.99, imageBytes34, 10, CategoryType.PANTALONES);
                productRepository.save(product34);

                byte[] imageBytes35 = loadImage("src/main/resources/images/pantalones/pantalon11.webp");
                Product product35 = new Product("Straight Cut", "Pantalón recto con diseño clásico.", 41.99, imageBytes35, 10, CategoryType.PANTALONES);
                productRepository.save(product35);

                byte[] imageBytes36 = loadImage("src/main/resources/images/pantalones/pantalon12.webp");
                Product product36 = new Product("Relaxed Fit", "Pantalón de corte relajado para máxima comodidad.", 37.99, imageBytes36, 10, CategoryType.PANTALONES);
                productRepository.save(product36);

                //JERSÉIS
                byte[] imageBytes37 = loadImage("src/main/resources/images/jerséis/jersey1.webp");
                Product product37 = new Product("Relaxed Fit", "Corte relajado para máxima comodidad.", 37.99, imageBytes37, 10, CategoryType.JERSEYS);
                productRepository.save(product37);

                byte[] imageBytes38 = loadImage("src/main/resources/images/jerséis/jersey2.webp");
                Product product38 = new Product("Classic Knit", "Jersey de punto con diseño clásico y elegante.", 42.99, imageBytes38, 10, CategoryType.JERSEYS);
                productRepository.save(product38);

                byte[] imageBytes39 = loadImage("src/main/resources/images/jerséis/jersey3.webp");
                Product product39 = new Product("Turtleneck Sweater", "Jersey de cuello alto para un estilo sofisticado.", 49.99, imageBytes39, 10, CategoryType.JERSEYS);
                productRepository.save(product39);

                byte[] imageBytes40 = loadImage("src/main/resources/images/jerséis/jersey4.webp");
                Product product40 = new Product("Chunky Knit", "Jersey grueso de punto con textura cálida.", 55.99, imageBytes40, 10, CategoryType.JERSEYS);
                productRepository.save(product40);

                byte[] imageBytes41 = loadImage("src/main/resources/images/jerséis/jersey5.webp");
                Product product41 = new Product("Cable Knit", "Jersey con diseño trenzado en el tejido.", 47.99, imageBytes41, 10, CategoryType.JERSEYS);
                productRepository.save(product41);

                byte[] imageBytes42 = loadImage("src/main/resources/images/jerséis/jersey6.webp");
                Product product42 = new Product("Slim Fit Sweater", "Jersey ajustado con diseño moderno.", 39.99, imageBytes42, 10, CategoryType.JERSEYS);
                productRepository.save(product42);

                byte[] imageBytes43 = loadImage("src/main/resources/images/jerséis/jersey7.webp");
                Product product43 = new Product("Oversized Knit", "Jersey oversized para un look relajado.", 50.99, imageBytes43, 10, CategoryType.JERSEYS);
                productRepository.save(product43);

                byte[] imageBytes44 = loadImage("src/main/resources/images/jerséis/jersey8.webp");
                Product product44 = new Product("Striped Sweater", "Jersey a rayas con diseño casual.", 44.99, imageBytes44, 10, CategoryType.JERSEYS);
                productRepository.save(product44);

                byte[] imageBytes45 = loadImage("src/main/resources/images/jerséis/jersey9.webp");
                Product product45 = new Product("Wool Blend", "Jersey de lana mezclada para mayor calidez.", 59.99, imageBytes45, 10, CategoryType.JERSEYS);
                productRepository.save(product45);

                byte[] imageBytes46 = loadImage("src/main/resources/images/jerséis/jersey10.webp");
                Product product46 = new Product("Mock Neck", "Jersey con cuello semi-alto y diseño minimalista.", 41.99, imageBytes46, 10, CategoryType.JERSEYS);
                productRepository.save(product46);

                byte[] imageBytes47 = loadImage("src/main/resources/images/jerséis/jersey11.webp");
                Product product47 = new Product("V-Neck Sweater", "Jersey de cuello en V, ideal para capas.", 43.99, imageBytes47, 10, CategoryType.JERSEYS);
                productRepository.save(product47);

                byte[] imageBytes48 = loadImage("src/main/resources/images/jerséis/jersey12.webp");
                Product product48 = new Product("Cozy Fleece", "Jersey de felpa suave para máxima comodidad.", 38.99, imageBytes48, 10, CategoryType.JERSEYS);
                productRepository.save(product48);


                logger.info("Productos cargados correctamente.");
            } catch (Exception e) {
                logger.error("Error al inicializar productos", e);
            }
        } else {
            logger.info("Los productos ya estaban en la base de datos. No se insertaron nuevos.");
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            logger.info("Cargando usuarios...");
            // Agregar lógica de creación de usuarios aquí
            logger.info("Usuarios cargados correctamente.");
        } else {
            logger.info("Los usuarios ya estaban en la base de datos.");
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
