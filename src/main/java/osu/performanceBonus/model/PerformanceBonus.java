package osu.performanceBonus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import osu.assignment.model.Assignment;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PerformanceBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount must not be null")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @FutureOrPresent(message = "End date must be in the future or present")
    private Date performanceBonusEligibilityDate;

    @NotNull(message = "Active state must not be null")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    @ToString.Exclude
    private Assignment assignment;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}