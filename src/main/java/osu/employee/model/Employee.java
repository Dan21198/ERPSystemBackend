package osu.employee.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import jakarta.validation.constraints.*;
import osu.assignment.model.Assignment;
import osu.user.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name must not be blank")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 100, message = "Title before name must not exceed 100 characters")
    private String titleBeforeName;

    @Size(max = 100, message = "Title after name must not exceed 100 characters")
    private String titleAfterName;

    @NotNull(message = "Contract start date must not be null")
    private Date contractStart;

    @FutureOrPresent(message = "Contract end date must be in the future or present")
    private Date contractEnd;

    @NotNull(message = "Workload percentage must not be null")
    @DecimalMin(value = "0", message = "Workload percentage must be at least 0")
    @DecimalMax(value = "100", message = "Workload percentage must not exceed 100")
    private Double workloadPercentage;

    @NotNull(message = "Gross salary must not be null")
    @PositiveOrZero(message = "Gross salary must be zero or positive")
    private Double grossSalary = 0.0;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Assignment> assignments = new HashSet<>();

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean contractAboutToExpire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    @ToString.Exclude
    private User createdBy;

    @PrePersist
    @PreUpdate
    public void updateContractExpirationStatus() {
        if (contractEnd != null) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = new java.sql.Date(contractEnd.getTime()).toLocalDate();
            LocalDate oneMonthBefore = endDate.minusMonths(1);
            this.contractAboutToExpire = !today.isBefore(oneMonthBefore) && !today.isAfter(endDate);
        } else {
            this.contractAboutToExpire = false;
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Employee employee = (Employee) o;
        return getId() != null && Objects.equals(getId(), employee.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}