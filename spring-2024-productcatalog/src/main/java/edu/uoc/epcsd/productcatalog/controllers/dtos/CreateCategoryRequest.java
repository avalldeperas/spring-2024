package edu.uoc.epcsd.productcatalog.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CreateCategoryRequest {

    private final String name;

    private final String description;

    private final Long parentId;

}