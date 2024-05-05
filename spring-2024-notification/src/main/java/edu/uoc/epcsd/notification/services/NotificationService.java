package edu.uoc.epcsd.notification.services;

import edu.uoc.epcsd.notification.kafka.ProductMessage;
import edu.uoc.epcsd.notification.rest.dtos.GetUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
@Component
public class NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    /**
     * Used RestTemplate with the userServiceUrl to query the User microservice in order to get the users
     * having an alert for the specified product & actual date (LocalDate.now). Email has been simulated as
     * requested in the exercise.
     *
     * @param productMessage Message from product catalog that indicates that there's a product available.
     */
    public void notifyProductAvailable(ProductMessage productMessage) {
        log.trace("NotificationService::notifyProductAvailable - message: {}", productMessage);

        Long productId = productMessage.getProductId();
        LocalDate availableOnDate = LocalDate.now();
        GetUserResponse[] usersToEmail = new RestTemplate()
                .getForObject(userServiceUrl, GetUserResponse[].class, productId, availableOnDate);

        Objects.requireNonNull(usersToEmail);

        Arrays.stream(usersToEmail).map(Objects::requireNonNull).forEach(this::sendEmail);

    }

    private void sendEmail(GetUserResponse response) {
        log.trace("Sending an email to user '{}'...", response.getFullName());
    }
}
