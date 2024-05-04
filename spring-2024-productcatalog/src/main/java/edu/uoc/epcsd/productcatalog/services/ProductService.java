package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.entities.Item;
import edu.uoc.epcsd.productcatalog.entities.OperationalStatus;
import edu.uoc.epcsd.productcatalog.model.ProductCriteria;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> findProductsByCriteria(ProductCriteria criteria) {

        if (criteria.getName() != null) {
            return productRepository.findByNameContaining(criteria.getName());
        }

        if (criteria.getCategoryId() != null) {
            return productRepository.findByCategoryId(criteria.getCategoryId());
        }

        if (criteria.getDescription() != null) {
            return productRepository.findByDescriptionContaining(criteria.getDescription());
        }

        if (criteria.getBrand() != null) {
            return productRepository.findByBrandContaining(criteria.getBrand());
        }

        if (criteria.getModel() != null) {
            return productRepository.findByModelContaining(criteria.getModel());
        }

        return Collections.emptyList();
    }


    public Product createProduct(Long categoryId, String name, String description, Double dailyPrice, String brand, String model) {

        Product product = Product.builder().name(name).description(description).dailyPrice(dailyPrice).brand(brand).model(model).status(OperationalStatus.OPERATIONAL).build();

        if (categoryId != null) {
            Optional<Category> category = categoryService.findById(categoryId);

            category.ifPresent(product::setCategory);
        }

        return productRepository.save(product);
    }

    public boolean deleteProduct(Long productId) {
        Optional<Product> productOp = findById(productId);
        if (productOp.isEmpty()) {
            log.trace("Product id {} not found", productId);
            return false;
        }

        Product product = productOp.get();
        List<Item> items = product.getItemList();

        product.setStatus(OperationalStatus.NON_OPERATIONAL);
        // TODO - ensure product does not have any compromised unit in future/current rents?
        // TODO - ensure that all units are set to non operational as expected?
        items.forEach(item -> item.setStatus(OperationalStatus.NON_OPERATIONAL));
        // this would save items too right?
        productRepository.save(product);

        return true;
    }
}
