package edu.uoc.epcsd.productcatalog.controllers.dtos;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class GetUserResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;

}
