package edu.uoc.epcsd.productcatalog.kafka;

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
