package es.codeurjc.webapp14.service;

import es.codeurjc.webapp14.model.Product;
import es.codeurjc.webapp14.model.Review;
import es.codeurjc.webapp14.model.Size;
import es.codeurjc.webapp14.model.Size.SizeName;
import es.codeurjc.webapp14.dto.BasicProductDTO;
import es.codeurjc.webapp14.dto.ProductDTO;
import es.codeurjc.webapp14.dto.ReviewDTO;
import es.codeurjc.webapp14.mapper.BasicProductMapper;
import es.codeurjc.webapp14.mapper.ProductMapper;
import es.codeurjc.webapp14.model.Order;
import es.codeurjc.webapp14.repository.OrderRepository;
import es.codeurjc.webapp14.repository.ProductRepository;
import es.codeurjc.webapp14.repository.ReviewRepository;
import es.codeurjc.webapp14.repository.SizeRepository;
import jakarta.persistence.EntityNotFoundException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;

@Service
public class ProductService {


    private final ReviewService reviewService;

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BasicProductMapper basicProductMapper;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository, ReviewService reviewService, BasicProductMapper basicProductMapper) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
        this.basicProductMapper = basicProductMapper;
    }

    public List<ProductDTO> getAllProducts() {
        return toDTOs(productRepository.findAll());
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return toDTO(product);

    }

    public List<ProductDTO> getAllProductsOutOfStock() {
        return toDTOs(productRepository.findByOutOfStockTrue());
    }

    public List<BasicProductDTO> getAllProductsSold() {
        return toBasicDTOs(productRepository.findTop4ByOrderBySoldDesc());
    }

    public List<BasicProductDTO> getTop5BestSellingProducts() {
        return toBasicDTOs(productRepository.findTop5ByOrderBySoldDesc());
    }

    public ProductDTO delete(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

		ProductDTO productDTO = toDTO(product);

        productRepository.deleteById(id);

		return productDTO;
    }

    public Page<BasicProductDTO> getProductsByCategory(String category, int page) {
        int pageSize = 4;
        Pageable pageable = PageRequest.of(page, pageSize);
        return productRepository.findByCategory(category.toUpperCase(), pageable)
       .map(basicProductMapper::toDTO);

    }

    public List<BasicProductDTO> searchProductsByName(String query) {
        return toBasicDTOs(productRepository.findByNameContainingIgnoreCase(query));
    }

    public Page<BasicProductDTO> getRecommendedProductsBasedOnLastOrder(Long userId, int page, int size) {
        Order lastOrder = orderRepository.findFirstByUserIdAndIsPaidTrueOrderByCreatedAtDesc(userId);

        if(lastOrder == null){
            lastOrder = orderRepository.findFirstByUserIdOrderByCreatedAtDesc(userId);
        }


        System.out.println("LAST ORDER: " + lastOrder.getId());

        List<String> categories = lastOrder.getOrderProducts().stream()
                .map(op -> op.getProduct().getCategory())
                .distinct()
                .collect(Collectors.toList());

        return productRepository.findRecommendedProductsByCategories(categories, userId, PageRequest.of(page, size)).map(basicProductMapper::toDTO);
    }

    public Page<ProductDTO> getProductsPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).map(productMapper::toDTO);
    }

    public int getTotalStockOfAllProducts() {
        return sizeRepository.getTotalStockOfAllProducts();
    }

    public int countProductsWithAllSizesOutOfStock() {
        return sizeRepository.countProductsWithAllSizesOutOfStock();
    }

    public List<ProductDTO> getProductsWithAllSizesOutOfStock() {
        return toDTOs(sizeRepository.findProductsWithAllSizesOutOfStock());
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public int getTotalStock() {
        return productRepository.findAll().stream()
                .mapToInt(Product::getStock)
                .sum();
    }

    public long getOutOfStockProductsCount() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() == 0)
                .count();
    }


    public Resource getProductImage(long id) throws SQLException {

		Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

		if (product.getImage() != null) {
			return new InputStreamResource(product.getImage().getBinaryStream());
		} else {
			throw new NoSuchElementException();
		}
	}



    private ProductDTO toDTO(Product product) {
		return productMapper.toDTO(product);
	}

	public Product toDomain(ProductDTO productDTO) {
		return productMapper.toDomain(productDTO);
	}

	private List<ProductDTO> toDTOs(List<Product> products) {
		return productMapper.toDTOs(products);
	}

    public Boolean getSize(Long id, String sizeName) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        for (Size size : product.getSizes()) {
            if (sizeName == "S" && size.getName() == SizeName.S && size.getStock() > 0)
                return true;
            if (sizeName == "M" && size.getName() == SizeName.M && size.getStock() > 0)
                return true;
            if (sizeName == "L" && size.getName() == SizeName.L && size.getStock() > 0)
                return true;
            if (sizeName == "XL" && size.getName() == SizeName.XL && size.getStock() > 0)
                return true;
        }
        return false;
    }

	public ProductDTO createOrReplaceProduct(Long id, ProductDTO productDTO, int stock_S, int stock_M,
        int stock_L, int stock_XL) {
            ProductDTO product;
            if(id == null) {
                product = createProduct(productDTO, stock_S, stock_M, stock_L, stock_XL);
            } else {
                product = replaceProduct(id, productDTO, stock_S, stock_M, stock_L, stock_XL);
            }
            return product;
        }

        private ProductDTO createProduct(ProductDTO productDTO, int stock_S, int stock_M, int stock_L, int stock_XL) {
        if (productDTO.id() != null) {
            throw new IllegalArgumentException();
        }

        Product product = toDomain(productDTO);
        
        product = productRepository.save(product);

        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size(Size.SizeName.S, stock_S, product));
        sizes.add(new Size(Size.SizeName.M, stock_M, product));
        sizes.add(new Size(Size.SizeName.L, stock_L, product));
        sizes.add(new Size(Size.SizeName.XL, stock_XL, product));

        product.setSizes(sizes);

        product = productRepository.save(product);

        return toDTO(product);
    }


    private ProductDTO replaceProduct(Long id, ProductDTO updatedProductDTO, int stock_S, int stock_M, int stock_L, int stock_XL) {
        Product oldProduct = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
		Product updatedProduct = toDomain(updatedProductDTO);
		updatedProduct.setId(id);

        updatedProduct.setImageBool(oldProduct.getImageBool());


		try {
            if (oldProduct.getImageBool() && updatedProduct.getImageBool() &&
                oldProduct.getImage() != null && oldProduct.getImage().length() > 0) {
        
                updatedProduct.setImage(BlobProxy.generateProxy(
                    oldProduct.getImage().getBinaryStream(),
                    oldProduct.getImage().length()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Review> reviews = reviewRepository.findByProductId(updatedProduct.getId());

        updatedProduct.setReviews(reviews);


        List<Size> sizes = oldProduct.getSizes();
        for(Size size : sizes){
            if(size.getName().equals(Size.SizeName.S)){
                size.setStock(stock_S);
                sizeRepository.save(size);
            }
            else if(size.getName().equals(Size.SizeName.M)){
                size.setStock(stock_M);
                sizeRepository.save(size);
            }
            else if(size.getName().equals(Size.SizeName.L)){
                size.setStock(stock_L);
                sizeRepository.save(size);
            }
            else if(size.getName().equals(Size.SizeName.XL)){
                size.setStock(stock_XL);
                sizeRepository.save(size);
            }
        }
        updatedProduct.setSizes(sizes);

        updatedProduct.setImageUrl(oldProduct.getImageUrl());


        productRepository.save(updatedProduct);

		return toDTO(updatedProduct);
    }

    public void createProductImageWeb(long id, InputStream inputStream, long size) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setImage(BlobProxy.generateProxy(inputStream, size));
        product.setImageBool(true);

        productRepository.save(product);
    }

    public void createProductImage(long id, URI location, InputStream inputStream, long size) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));


        location = fromCurrentRequest().build().toUri();

        String newLocationStr = location.toString().replace("/admin/", "/");

        URI newLocation = URI.create(newLocationStr);

        product.setImageUrl(newLocation.toString());
        product.setImage(BlobProxy.generateProxy(inputStream, size));
        product.setImageBool(true);

        productRepository.save(product);
    }

    public List<String> getTop5ProductNames() {
        return productRepository.findTop5ByOrderBySoldDesc().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }
    
    public List<Integer> getTop5ProductSales() {
        return productRepository.findTop5ByOrderBySoldDesc().stream()
                .map(Product::getSold)
                .collect(Collectors.toList());
    }

    public Page<ReviewDTO> getTwoReviews(int page, int size, ProductDTO productDTO) {
        Product product = toDomain(productDTO);
        Pageable pageable = PageRequest.of(page, size);
        return reviewService.getTwoReviews(product, pageable);
    }

    public void deleteProductImage(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

		if (product.getImageUrl() == null) {
			throw new EntityNotFoundException("Image not found");
		}

		product.setImage(null);
        product.setImageUrl(null);
		product.setImageBool(false);

	    productRepository.save(product);
    }

    private List<BasicProductDTO> toBasicDTOs(List<Product> products) {
        return basicProductMapper.toDTOs(products);
    }

    public void replaceProductImage(long id, InputStream inputStream, long size) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if(product.getImage() == null){
            throw new NoSuchElementException();
        }
        product.setImage(BlobProxy.generateProxy(inputStream, size));
        productRepository.save(product);
    }
    
    
}
