package edu.uoc.epcsd.productcatalog.entities;

import edu.uoc.epcsd.productcatalog.model.OfferStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    // TODO: User is not in this module. Can this be userId?
    private Long userId;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Product product;

    private String serialNumber;
    private OfferStatus status;
    private LocalDate date;
}
