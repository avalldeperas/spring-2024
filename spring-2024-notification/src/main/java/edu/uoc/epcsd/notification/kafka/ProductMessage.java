package edu.uoc.epcsd.notification.kafka;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMessage {
    
    private Long productId;

}
