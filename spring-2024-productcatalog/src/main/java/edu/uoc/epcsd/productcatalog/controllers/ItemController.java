package edu.uoc.epcsd.productcatalog.controllers;


import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateItemRequest;
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
    public ResponseEntity<Item> getItemById(@PathVariable @NotNull String serialNumber) {
        log.trace("getItemById");

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

    // TODO: add the code for the missing system operations here:
    // 1. setOperational
    //  * use the correct HTTP verb
    //  * must ensure the item exists
    //  * if the new status is OPERATIONAL, must send a UNIT_AVAILABLE message to the kafka message queue (see ItemService.createItem method)

}
