package osu.assignment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import osu.employee.model.Employee;
import osu.position.model.Position;
import osu.tariff.model.Tariff;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    private LocalDate startDate;
    private LocalDate endDate;

    @Min(0)
    @Max(100)
    private Integer allocatedTimePercentage;

    @NotBlank
    @Size(max = 50)
    private String positionName;

    @PrePersist
    @PreUpdate
    private void syncPositionName() {
        if (position != null) {
            this.positionName = position.getName();
        }
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (startDate != null && !now.isBefore(startDate)) &&
                (endDate == null || !now.isAfter(endDate));
    }

}