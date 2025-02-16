package osu.position.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.employee.model.Employee;
import osu.project.model.Project;
import osu.tariff.model.Tariff;

@Entity
@Data
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Position name must not be blank")
    @Size(max = 50, message = "Position name must not exceed 50 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

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
    private Project project;
}