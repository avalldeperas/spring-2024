package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        if (parentId != null) {
            Optional<Category> parent = categoryRepository.findById(parentId);

            if (parent.isPresent()) {
                category.setParent(parent.get());
            }
        }

        return categoryRepository.save(category);
    }
}
