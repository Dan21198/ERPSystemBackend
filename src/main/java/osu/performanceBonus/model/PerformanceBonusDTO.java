package osu.performanceBonus.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PerformanceBonusDTO {
    private Long id;

    private Long assignmentId;

    @NotNull(message = "Amount must not be null")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @FutureOrPresent(message = "End date must be in the future or present")
    private Date performanceBonusEligibilityDate;

    @NotNull(message = "Active state must not be null")
    private Boolean isActive;
}
