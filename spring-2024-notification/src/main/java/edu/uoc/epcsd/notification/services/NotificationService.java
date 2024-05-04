package edu.uoc.epcsd.notification.services;

import edu.uoc.epcsd.notification.kafka.ProductMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    public void notifyProductAvailable(ProductMessage productMessage) {

        // TODO: Use RestTemplate with the above userServiceUrl to query the User microservice in order to get the users that have an alert for the specified product (the date specified in the parameter may be the actual date: LocalDate.now()).
        //  Then simulate the email notification for the alerted users by logging a line with INFO level for each user saying "Sending an email to user " + the user fullName

    }
}
