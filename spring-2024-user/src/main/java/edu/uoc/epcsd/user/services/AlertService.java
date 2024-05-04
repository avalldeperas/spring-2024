package edu.uoc.epcsd.user.services;

import edu.uoc.epcsd.user.controllers.dtos.GetProductResponse;
import edu.uoc.epcsd.user.entities.Alert;
import edu.uoc.epcsd.user.entities.User;
import edu.uoc.epcsd.user.repositories.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserService userService;

    @Value("${productCatalog.getProductDetails.url}")
    private String productCatalogUrl;


    public List<Alert> findAll() {
        return alertRepository.findAll();
    }

    public Optional<Alert> findById(Long id) {
        return alertRepository.findById(id);
    }

    public Alert createAlert(Long productId, Long userId, LocalDate from, LocalDate to) {

        Alert alert = Alert.builder().from(from).to(to).build();

        Optional<User> user = userService.findById(userId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("A valid userId parameter is mandatory");
        }

        alert.setUser(user.get());

        try {
            // verify the specified product exists in product service
            ResponseEntity<GetProductResponse> getProductResponseEntity = new RestTemplate().getForEntity(productCatalogUrl, GetProductResponse.class, productId);

            alert.setProductId(productId);

        } catch (RestClientException e) {
            throw new IllegalArgumentException("Could not found the productId: " + productId, e);
        }

        return alertRepository.save(alert);
    }

    public List<Alert> findAlertsByProductIdAndDate(Long productId, LocalDate from) {
        return alertRepository.findAlertsByProductIdAndInterval(productId, from);
    }

    public List<Alert> findAlertsByUserAndInterval(Long userId, LocalDate from, LocalDate to) {
        return alertRepository.findAlertsByUserAndInterval(userId, from, to);
    }
}
