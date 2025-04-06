package osu.position.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.assignment.model.Assignment;
import osu.performanceBonus.model.PerformanceBonus;
import osu.project.model.Project;
import osu.user.model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private Project project;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer allocatedTimePercentage;

    private Double totalAmountSpent = 0.0;

    @OneToMany(mappedBy = "position", cascade = CascadeType.PERSIST)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    @ToString.Exclude
    private User createdBy;

    @Transient
    public Long getDurationInMonths() {
        if (startDate != null && endDate != null) {
            return ChronoUnit.MONTHS.between(startDate, endDate);
        }
        return null;
    }

    @Transient
    public Double getFte() {
        if (allocatedTimePercentage != null && getDurationInMonths() != null) {
            return (getDurationInMonths() * allocatedTimePercentage) / 100.0;
        }
        return null;
    }

    public void updateTotalAmountSpent() {
        if (this.assignments == null || this.assignments.isEmpty()) {
            this.totalAmountSpent = 0.0;
            return;
        }

        this.totalAmountSpent = this.assignments.stream()
                .filter(a -> a.getTariff() != null)
                .mapToDouble(this::calculateAssignmentCost)
                .sum();
    }

    private double calculateAssignmentCost(Assignment assignment) {
        if (assignment.getTariff() == null || assignment.getAllocatedTimePercentage() == null) {
            return 0.0;
        }

        LocalDate effectiveStart = assignment.getStartDate();
        LocalDate effectiveEnd = assignment.isActive() ?
                (assignment.getEndDate() != null ? assignment.getEndDate() : LocalDate.now()) :
                assignment.getEndDate();

        if (effectiveStart == null || effectiveEnd == null || effectiveStart.isAfter(effectiveEnd)) {
            return 0.0;
        }

        long days = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
        double avgDaysPerMonth = 30.44;
        double monthlyCost = assignment.getTariff().getWageTariff() *
                (assignment.getAllocatedTimePercentage() / 100.0);

        double bonusCost = assignment.getPerformanceBonuses().stream()
                .filter(PerformanceBonus::getIsActive)
                .mapToDouble(PerformanceBonus::getAmount)
                .sum();

        return ((monthlyCost / avgDaysPerMonth) * days) + bonusCost;
    }
}