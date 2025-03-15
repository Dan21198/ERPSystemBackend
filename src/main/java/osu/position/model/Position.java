package osu.position.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.employee.model.Employee;
import osu.position.validator.DateConstraint;
import osu.project.model.Project;
import osu.tariff.model.Tariff;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@NoArgsConstructor
@DateConstraint
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Position name must not be blank")
    @Size(max = 50, message = "Position name must not exceed 50 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    @ToString.Exclude
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    @ToString.Exclude
    private Employee employee;

    private LocalDate startDate;

    private LocalDate endDate;

    @Min(value = 0, message = "Allocated time percentage must be at least 0")
    @Max(value = 100, message = "Allocated time percentage must be at most 100")
    private Integer allocatedTimePercentage;

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

    public void setEmployee(Employee employee) {
        if (this.employee != null) {
            this.employee.getPositions().remove(this);
        }
        this.employee = employee;
        if (employee != null) {
            employee.getPositions().add(this);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference
    @ToString.Exclude
    private Project project;
}