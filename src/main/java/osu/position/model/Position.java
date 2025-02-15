package osu.position.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.employee.model.Employee;
import osu.project.model.Project;

import java.util.Set;

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

    @ManyToMany(mappedBy = "positions")
    @JsonManagedReference
    private Set<Employee> employees;

    @ManyToMany(mappedBy = "positions")
    @JsonBackReference
    private Set<Project> projects;
}