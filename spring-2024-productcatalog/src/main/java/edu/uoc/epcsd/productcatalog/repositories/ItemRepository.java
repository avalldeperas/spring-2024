package edu.uoc.epcsd.productcatalog.repositories;

import edu.uoc.epcsd.productcatalog.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, String> {

    Optional<Item> findBySerialNumber(String serialNumber);

}
