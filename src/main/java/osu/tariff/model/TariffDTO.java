package osu.tariff.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TariffDTO {
    private Long id;

    @NotNull(message = "Wage class cannot be blank")
    private Integer wageClass;

    @NotNull(message = "Position title cannot be blank")
    private String positionTitle;

    @NotNull(message = "Wage tariff cannot be blank")
    private Double wageTariff;

    @NotNull(message = "Valid from date cannot be blank")
    private Date validFrom;

    @NotNull(message = "Valid to date cannot be blank")
    private Date validTo;

}