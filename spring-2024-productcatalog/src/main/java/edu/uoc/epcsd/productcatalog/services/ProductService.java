package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.model.Criteria;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

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

    public List<Product> findProductsByCriteria(Criteria criteria) {

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

        Product product = Product.builder().name(name).description(description).dailyPrice(dailyPrice).brand(brand).model(model).build();

        if (categoryId != null) {
            Optional<Category> category = categoryService.findById(categoryId);

            if (category.isPresent()) {
                product.setCategory(category.get());
            }
        }

        return productRepository.save(product);
    }
}
