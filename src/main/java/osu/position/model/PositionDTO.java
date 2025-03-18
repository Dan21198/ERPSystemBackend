package osu.position.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.position.validator.DateConstraint;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DateConstraint
public class PositionDTO {
    private Long id;

    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long durationInMonths;

    @Min(value = 0, message = "Allocated time percentage must be at least 0")
    @Max(value = 100, message = "Allocated time percentage must be at most 100")
    private Integer allocatedTimePercentage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double fte;

}