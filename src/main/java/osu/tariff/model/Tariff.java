package osu.tariff.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import osu.assignment.model.Assignment;
import osu.user.model.User;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Wage class cannot be blank")
    private Integer wageClass;

    @NotBlank(message = "Position title cannot be blank")
    private String positionTitle;

    @NotNull(message = "Wage tariff cannot be blank")
    private Double wageTariff;

    @NotNull(message = "Valid from date cannot be blank")
    private Date validFrom;

    @NotNull(message = "Valid to date cannot be blank")
    private Date validTo;

    @OneToMany(mappedBy = "tariff")
    private Set<Assignment> assignments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    @ToString.Exclude
    private User createdBy;
}