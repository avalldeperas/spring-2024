package edu.uoc.epcsd.productcatalog.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryCriteria {
    private String name;
    private String description;
    private Long parent;
}
