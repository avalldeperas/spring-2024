package edu.uoc.epcsd.productcatalog.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public final class CreateOfferRequest {

    @NonNull
    private final String serialNumber;

    @NonNull
    private final Long userId;

    @NonNull
    private final Long categoryId;

    @NonNull
    private final Long productId;

    @NonNull
    private final Double dailyPrice;

}