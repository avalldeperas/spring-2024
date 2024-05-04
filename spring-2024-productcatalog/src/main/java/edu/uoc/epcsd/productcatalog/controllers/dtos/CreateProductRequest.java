package edu.uoc.epcsd.productcatalog.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CreateProductRequest {

    private final String name;

    private final String description;

    private final Long categoryId;

    private final Double dailyPrice;

    private final String brand;

    private final String model;
}
