package edu.uoc.epcsd.productcatalog.services;

import edu.uoc.epcsd.productcatalog.controllers.dtos.GetUserResponse;
import edu.uoc.epcsd.productcatalog.entities.*;
import edu.uoc.epcsd.productcatalog.kafka.KafkaConstants;
import edu.uoc.epcsd.productcatalog.kafka.ProductMessage;
import edu.uoc.epcsd.productcatalog.model.OfferStatus;
import edu.uoc.epcsd.productcatalog.repositories.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    @Value("${userService.getUserById.url}")
    private String userServiceUrl;

    private static final Logger log = LoggerFactory.getLogger(OfferService.class);

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public Offer createOffer(Long categoryId, Long productId, String serialNumber, Long userId) {
        Long userIdFound = validateUser(userId);
        Category category = validateCategory(categoryId);
        Product product = validateProduct(productId);

        Offer offer = buildOffer(category, product, serialNumber, userIdFound);

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

    public List<Offer> findOffersByUser(Long userId) {
        return offerRepository.findByUserId(userId);
    }

    private Offer buildOffer(Category category, Product product, String serialNumber, Long userId) {
        return Offer.builder()
                .category(category)
                .product(product)
                .serialNumber(serialNumber)
                .userId(userId)
                .build();
    }

    private Product validateProduct(Long productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty())
            throw new IllegalArgumentException(String.format("Product id %d does not exist", productId));
        return product.get();
    }

    private Category validateCategory(Long categoryId) {
        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isEmpty())
            throw new IllegalArgumentException(String.format("Category id %d does not exist", categoryId));
        return category.get();
    }

    private Long validateUser(Long userId) {
        ResponseEntity<GetUserResponse> userResponse;
        try {
            userResponse = new RestTemplate().getForEntity(userServiceUrl, GetUserResponse.class, userId);
            return Objects.requireNonNull(userResponse.getBody()).getId();
        } catch (RestClientException e) {
            throw new IllegalArgumentException(String.format("Could not found the userId %d", userId), e);
        }
    }
}
