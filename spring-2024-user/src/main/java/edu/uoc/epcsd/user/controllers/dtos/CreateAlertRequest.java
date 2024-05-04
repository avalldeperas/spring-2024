package edu.uoc.epcsd.user.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public final class CreateAlertRequest {

    @NonNull
    private final Long productId;

    @NonNull
    private final Long userId;

    @NonNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private final LocalDate from;

    @NonNull
    @JsonFormat(pattern="dd-MM-yyyy")
    private final LocalDate to;
}
