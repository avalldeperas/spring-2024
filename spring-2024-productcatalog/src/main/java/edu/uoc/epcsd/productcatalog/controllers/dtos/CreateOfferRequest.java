package edu.uoc.epcsd.productcatalog.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class CreateOfferRequest {

    private final String serialNumber;
    private final String email;
    private final Long categoryId;
    private final Long productId;
}