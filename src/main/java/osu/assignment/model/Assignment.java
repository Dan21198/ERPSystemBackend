package osu.assignment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import osu.employee.model.Employee;
import osu.performanceBonus.model.PerformanceBonus;
import osu.position.model.Position;
import osu.tariff.model.Tariff;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PerformanceBonus> performanceBonuses = new HashSet<>();

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(0)
    @Max(100)
    private Integer allocatedTimePercentage;

    @NotBlank
    @Size(max = 50)
    private String positionName;

    private boolean active;

    @PrePersist
    @PreUpdate
    private void syncPositionName() {
        if (position != null) {
            this.positionName = position.getName();
        }
    }
}