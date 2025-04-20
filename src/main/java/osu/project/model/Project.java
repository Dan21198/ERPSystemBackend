package osu.project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import osu.contract.model.Contract;
import osu.position.model.Position;
import osu.user.model.User;
import osu.project.enums.ProjectStatus;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project code must not be blank")
    @Size(max = 50, message = "Project code must not exceed 50 characters")
    private String projectCode;

    @NotBlank(message = "Project name must not be blank")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String projectName;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    private Date projectStart;

    private Date projectEnd;

    private Double totalAmountSpent = 0.0;

    public Double getTotalAmountSpent() {
        if (this.positions == null || this.positions.isEmpty()) {
            return 0.0;
        }

        return this.positions.stream()
                .filter(Objects::nonNull)
                .mapToDouble(position -> {
                    Double amount = position.getTotalAmountSpent();
                    return amount != null ? amount : 0.0;
                })
                .sum();
    }

    public void updateTotalAmountSpent() {
        this.totalAmountSpent = getTotalAmountSpent();
    }

    private Double totalAmountAllocated = 0.0;

    public Double getTotalAmountAllocated() {
        if (this.contracts == null || this.contracts.isEmpty()) {
            return 0.0;
        }

        return this.contracts.stream()
                .mapToDouble(Contract::getAvailableAmount)
                .sum();
    }

    public void updateTotalAmountAllocated() {
        this.totalAmountAllocated = getTotalAmountAllocated();
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private Set<Position> positions = new HashSet<>();

    public void setPositions(Set<Position> positions) {
        this.positions.clear();
        if (positions != null) {
            this.positions.addAll(positions);
            positions.forEach(position -> position.setProject(this));
        }
    }

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private Set<Contract> contracts = new HashSet<>();

    public void setProjectName(String projectName) {
        this.projectName = projectName;
        updateContractOrderNames();
    }

    private void updateContractOrderNames() {
        if (this.contracts != null) {
            this.contracts.forEach(Contract::synchronizeOrderName);
        }
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("projects")
    @ToString.Exclude
    private Set<User> users;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Project project = (Project) o;
        return getId() != null && Objects.equals(getId(), project.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}