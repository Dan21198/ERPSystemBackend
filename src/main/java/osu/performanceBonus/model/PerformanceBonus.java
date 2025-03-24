package osu.performanceBonus.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import osu.employee.model.Employee;

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

    @NotNull(message = "Start date must not be null")
    private Date startDate;

    @FutureOrPresent(message = "End date must be in the future or present")
    private Date endDate;

    @NotNull(message = "Active state must not be null")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}
