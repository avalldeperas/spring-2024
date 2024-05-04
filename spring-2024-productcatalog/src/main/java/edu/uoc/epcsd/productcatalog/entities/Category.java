package edu.uoc.epcsd.productcatalog.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
public class Category extends CatalogElement {

    @ManyToOne
    private Category parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<Category> children;

}
