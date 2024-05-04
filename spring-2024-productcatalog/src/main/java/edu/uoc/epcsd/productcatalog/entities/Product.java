package edu.uoc.epcsd.productcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends CatalogElement {

    @Column(name = "dailyPrice", nullable = false)
    private Double dailyPrice;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @ManyToOne(optional = false)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Item> itemList;

}
