package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.entities.*;
import edu.uoc.epcsd.productcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.productcatalog.kafka.ProductMessage;
import edu.uoc.epcsd.productcatalog.model.OfferStatus;
import edu.uoc.epcsd.productcatalog.repositories.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, ProductMessage> productKafkaTemplate;

    private static final Logger log = LoggerFactory.getLogger(OfferService.class);

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public Offer createOffer(Long categoryId, Long productId, String serialNumber, String email) {
        // TODO is this check necessary? If we are able to find product, doesn't that mean that category exist?
        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isEmpty())
            throw new IllegalArgumentException(String.format("Category id %d does not exist", categoryId));

        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty())
            throw new IllegalArgumentException(String.format("Product id %d does not exist", productId));

        Offer offer = buildOffer(category.get(), serialNumber, email, product.get());

        return offerRepository.save(offer);
    }

    public Offer evaluateOffer(Long offerId, LocalDate date, OfferStatus status) {
        Optional<Offer> offer = offerRepository.findById(offerId);
        if (offer.isEmpty())
            throw new IllegalArgumentException(String.format("Offer id %d does not exist", offerId));

        offer.get().setStatus(status);
        offer.get().setDate(date);

        if (status.isAccepted()) {
            ProductMessage message = ProductMessage.builder().productId(offer.get().getProduct().getId()).build();
            log.info("Message produced: {}", message);
            productKafkaTemplate.send(KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, message);
        }

        return offerRepository.save(offer.get());
    }

    private Offer buildOffer(Category category, String serialNumber, String email, Product product) {
        return Offer.builder()
                .category(category)
                .product(product)
                .serialNumber(serialNumber)
                .email(email)
                .build();
    }
}
