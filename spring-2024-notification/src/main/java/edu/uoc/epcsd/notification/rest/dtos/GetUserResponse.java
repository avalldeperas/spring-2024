package edu.uoc.epcsd.notification.rest.dtos;

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
