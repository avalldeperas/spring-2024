package edu.uoc.epcsd.productcatalog.entities;

import edu.uoc.epcsd.productcatalog.model.OperationalStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @Column(name = "serialNumber", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationalStatus status;

    @ManyToOne
    private Product product;
}
