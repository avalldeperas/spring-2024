package edu.uoc.epcsd.user.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`from`", nullable = false, columnDefinition = "DATE")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate from;

    @Column(name = "`to`", nullable = false, columnDefinition = "DATE")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate to;

    @Column(name = "productId", nullable = false)
    private Long productId;

    @ManyToOne
    private User user;

}
