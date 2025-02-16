package osu.position.model;

import jakarta.validation.constraints.Size;
import lombok.*;
import osu.employee.model.EmployeeDTO;
import osu.tariff.model.TariffDTO;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PositionDTO {
    private Long id;

    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    private TariffDTO tariff;
    private EmployeeDTO employee;
}