package edu.uoc.epcsd.user.controllers.dtos;

import edu.uoc.epcsd.user.entities.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public final class GetUserResponse {

    private final Long id;

    private final String fullName;

    private final String email;

    private final String phoneNumber;

    public static GetUserResponse fromDomain(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
