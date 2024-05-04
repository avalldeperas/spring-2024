package edu.uoc.epcsd.productcatalog.model;

import lombok.*;

@Getter
@Builder
public class Criteria {

    private String name;
    private String description;
    private String brand;
    private String model;
    private Long categoryId;
}
