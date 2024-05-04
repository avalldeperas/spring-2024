package edu.uoc.epcsd.productcatalog.controllers;


import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateProductRequest;
import edu.uoc.epcsd.productcatalog.controllers.dtos.GetProductResponse;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.services.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        log.trace("getAllProducts");

        return productService.findAll();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetProductResponse> getProductById(@PathVariable @NotNull Long productId) {
        log.trace("getProductById");

        return productService.findById(productId).map(product -> ResponseEntity.ok().body(GetProductResponse.fromDomain(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        log.trace("createProduct");

        log.trace("Creating product " + createProductRequest);
        Long productId = productService.createProduct(
                createProductRequest.getCategoryId(),
                createProductRequest.getName(),
                createProductRequest.getDescription(),
                createProductRequest.getDailyPrice(),
                createProductRequest.getBrand(),
                createProductRequest.getModel()).getId();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(uri).body(productId);
    }

    // TODO: add the code for the missing system operations here:
    // 1. remove product (use DELETE HTTP verb). Must remove the associated items
    // 2. query products by name
    // 3. query products by category/subcategory

}
