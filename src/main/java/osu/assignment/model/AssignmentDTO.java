package osu.assignment.model;

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


    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer allocatedTimePercentage;

    private boolean active;
}
