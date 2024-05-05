package edu.uoc.epcsd.productcatalog.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uoc.epcsd.productcatalog.model.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public final class EvaluateOfferRequest {

    @NonNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private final LocalDate date;

    @NonNull
    private final OfferStatus status;
}