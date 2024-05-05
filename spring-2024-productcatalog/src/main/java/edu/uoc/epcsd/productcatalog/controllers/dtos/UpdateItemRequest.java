package edu.uoc.epcsd.productcatalog.controllers.dtos;

import edu.uoc.epcsd.productcatalog.model.OperationalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public final class UpdateItemRequest {

    @NonNull
    private final String serialNumber;

    @NonNull
    private final OperationalStatus status;
}
