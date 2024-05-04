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
        if (itemOp.isEmpty()) {
            log.info("Item {} does not exist", serialNumber);
            return null;
        }

        Item item = itemOp.get();
        if (status == item.getStatus()) {
            log.info("Item {} already has status {}", serialNumber, status);
            return null;
        }

        // TODO check unit not compromised in any current or future rent when operational false
        item.setStatus(status);
        itemRepository.save(item);

        if (status.isOperational()) {
            ProductMessage message = ProductMessage.builder().productId(item.getProduct().getId()).build();
            log.info("I am about to send message {}", message);
            productKafkaTemplate.send(KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, message);
        }

        return item;
    }

    public Item createItem(Long productId, String serialNumber) {

        // bu default a new unit is OPERATIONAL
        Item item = Item.builder().serialNumber(serialNumber).status(OperationalStatus.OPERATIONAL).build();

        Optional<Product> product = productService.findById(productId);

        if (product.isEmpty()) {
            throw new IllegalArgumentException("Could not find the product with Id: " + productId);
        }

        item.setProduct(product.get());
        Item savedItem = itemRepository.save(item);

        ProductMessage productMessage = ProductMessage.builder().productId(productId).build();
        productKafkaTemplate.send(KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, productMessage);

        return savedItem;
    }
}
