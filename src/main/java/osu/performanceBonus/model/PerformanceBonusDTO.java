package osu.performanceBonus.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate performanceBonusEligibilityDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;
}
