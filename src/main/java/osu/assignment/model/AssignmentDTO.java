package osu.assignment.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import osu.performanceBonus.model.PerformanceBonusDTO;

import java.time.LocalDate;
import java.util.Set;

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

    private Set<PerformanceBonusDTO> performanceBonuses;

    private boolean active;
}