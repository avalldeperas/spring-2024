package edu.uoc.epcsd.productcatalog.controllers;


import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateItemRequest;
import edu.uoc.epcsd.productcatalog.controllers.dtos.UpdateItemRequest;
import edu.uoc.epcsd.productcatalog.entities.Item;
import edu.uoc.epcsd.productcatalog.services.ItemService;
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
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getAllItems() {
        log.trace("getAllItems");

        return itemService.findAll();
    }

    @GetMapping("/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Item> getItemBySerialNumber(@PathVariable @NotNull String serialNumber) {
        log.trace("getItemBySerialNumber");

        return itemService.findBySerialNumber(serialNumber).map(item -> ResponseEntity.ok().body(item))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody CreateItemRequest createItemRequest) {
        log.trace("createItem");

        log.trace("Creating item " + createItemRequest);
        String serialNumber = itemService.createItem(createItemRequest.getProductId(),
                createItemRequest.getSerialNumber()).getSerialNumber();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{serialNumber}")
                .buildAndExpand(serialNumber)
                .toUri();

        return ResponseEntity.created(uri).body(serialNumber);
    }

    /**
     * Updates item to set its status (operational/non-operational). Ensures that item exists and also that product does
     * not have same status already. If status is OPERATIONAL, a UNIT_AVAILABLE message is sent to the kafka message
     * queue
     *
     * @param updateItemRequest request with operational status and item serial number to update.
     * @return ResponseEntity with item if success, null otherwise
     */
    @PatchMapping
    public ResponseEntity<String> updateItem(@RequestBody UpdateItemRequest updateItemRequest) {
        log.trace("updateItem - request = '{}'", updateItemRequest);

        Item item = itemService.setOperational(updateItemRequest.getSerialNumber(), updateItemRequest.getStatus());

        return ResponseEntity.ok(item.getSerialNumber());
    }
}
