package osu.position.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.employee.model.EmployeeDTO;
import osu.tariff.model.TariffDTO;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PositionDTO {
    private Long id;

    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long durationInMonths;

    private TariffDTO tariff;
    private EmployeeDTO employee;
}