package edu.uoc.epcsd.productcatalog.repositories;

import edu.uoc.epcsd.productcatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String name);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByDescriptionContaining(String description);

    List<Product> findByBrandContaining(String brand);

    List<Product> findByModelContaining(String model);

}
