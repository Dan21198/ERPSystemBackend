package osu.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    private Long id;

    @NotNull
    private Long employeeId;

    @NotNull
    private Long positionId;

    @NotNull
    private Long tariffId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate startDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate endDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer allocatedTimePercentage;

    private boolean isActive;
}
