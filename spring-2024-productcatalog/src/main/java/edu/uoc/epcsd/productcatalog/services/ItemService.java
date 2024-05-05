package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.Item;
import edu.uoc.epcsd.productcatalog.model.OperationalStatus;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.productcatalog.kafka.ProductMessage;
import edu.uoc.epcsd.productcatalog.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemService.class);
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, ProductMessage> productKafkaTemplate;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findBySerialNumber(String serialNumber) {
        return itemRepository.findBySerialNumber(serialNumber);
    }

    public Item setOperational(String serialNumber, OperationalStatus status) {
        Optional<Item> itemOp = findBySerialNumber(serialNumber);
        if (itemOp.isEmpty())
            throw new IllegalArgumentException(String.format("Item serial number '%s' does not exist", serialNumber));

        // As requested in PRAC1 solution, added a check to ensure we don't set the same status to the item again.
        Item item = itemOp.get();
        if (status == item.getStatus())
            throw new IllegalArgumentException(String.format("Item serial number '%s' already has status '%s'", serialNumber, status));

        item.setStatus(status);
        itemRepository.save(item);

        if (status.isOperational()) {
            ProductMessage message = buildMessage(item.getProduct().getId());
            log.trace("I am about to send message {}", message);
            productKafkaTemplate.send(KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, message);
        }

        return item;
    }

    public Item createItem(Long productId, String serialNumber) {
        Product product = validateProduct(productId);
        // Adding additional check required in PRAC1 solution, as serial number must not exist
        validateSerialNumber(serialNumber);

        // By default a new unit is OPERATIONAL
        Item item = Item.builder().serialNumber(serialNumber).status(OperationalStatus.OPERATIONAL).product(product).build();
        Item savedItem = itemRepository.save(item);

        productKafkaTemplate.send(KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, buildMessage(productId));

        return savedItem;
    }

    private ProductMessage buildMessage(Long productId) {
        return ProductMessage.builder().productId(productId).build();
    }

    private void validateSerialNumber(String serialNumber) {
        Optional<Item> item = itemRepository.findBySerialNumber(serialNumber);
        if (item.isPresent())
            throw new IllegalArgumentException(String.format("Serial number '%s' already exists.", serialNumber));
    }

    private Product validateProduct(Long productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty())
            throw new IllegalArgumentException("Could not find the product with Id: " + productId);
        return product.get();
    }
}
