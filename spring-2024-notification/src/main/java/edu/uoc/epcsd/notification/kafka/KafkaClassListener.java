package edu.uoc.epcsd.notification.kafka;

import edu.uoc.epcsd.notification.services.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class KafkaClassListener {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, groupId = "group-1")
    void productAvailable(ProductMessage productMessage) {
        log.trace("productAvailable");

        notificationService.notifyProductAvailable(productMessage);
    }
}
