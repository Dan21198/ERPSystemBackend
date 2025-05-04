package osu.position.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.assignment.model.Assignment;
import osu.project.model.Project;
import osu.user.model.User;

import java.time.LocalDate;
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

    private Double allowedSpentAmount = 0.0;

    private Boolean isOverAllowedSpentAmount = false;

    @OneToMany(mappedBy = "position", cascade = CascadeType.PERSIST)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    @ToString.Exclude
    private User createdBy;
}