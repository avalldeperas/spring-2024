package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateCategoryRequest;
import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.model.CategoryCriteria;
import edu.uoc.epcsd.productcatalog.services.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return categoryService.findAll();
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        log.trace("createCategory");

        log.trace("Creating category " + createCategoryRequest);
        Long categoryId = categoryService.createCategory(
                createCategoryRequest.getParentId(),
                createCategoryRequest.getName(),
                createCategoryRequest.getDescription()).getId();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryId)
                .toUri();

        return ResponseEntity.created(uri).body(categoryId);
    }

    /**
     * Single endpoint that queries categories by different criteria.
     * @param name category name
     * @param description category description
     * @param parent category parent
     * @return list of categories that matches criteria received.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> findCategoriesByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long parent
    ) {
        log.trace("findCategoriesByCriteria - name = {}, description = {}, parent = {}", name, description, parent);

        CategoryCriteria criteria = CategoryCriteria.builder().name(name).description(description).parent(parent).build();

        return categoryService.findByCriteria(criteria);
    }
}
