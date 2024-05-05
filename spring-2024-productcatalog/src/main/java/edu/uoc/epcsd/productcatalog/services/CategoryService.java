package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.model.CategoryCriteria;
import edu.uoc.epcsd.productcatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Long parentId, String name, String description) {
        Category category = Category.builder().name(name).description(description).build();

        if (parentId != null) categoryRepository.findById(parentId).ifPresent(category::setParent);

        return categoryRepository.save(category);
    }

    public List<Category> findByCriteria(CategoryCriteria criteria) {
        if (criteria.getName() != null) {
            return categoryRepository.findByNameContaining(criteria.getName());
        }

        if (criteria.getDescription() != null) {
            return categoryRepository.findByDescriptionContaining(criteria.getDescription());
        }

        if (criteria.getParent() != null) {
            return categoryRepository.findByParentId(criteria.getParent());
        }

        return Collections.emptyList();
    }
}
