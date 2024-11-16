package osu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import osu.enums.AcademicTitle;

import java.util.Date;
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
    private Long personalNumber;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private AcademicTitle title;

    private Date contractStart;
    private Date contractEnd;
    private Double workloadPercentage;
    private String salaryGrade;
    private Double tariffAmount;
    private Double performanceBonus;
    private Double grossSalary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    @JsonBackReference
    private Position position;

    @ManyToMany(mappedBy = "employees")
    @JsonIgnore
    private Set<Project> projects;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    private User createdBy;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Employee employee = (Employee) o;
        return getPersonalNumber() != null && Objects.equals(getPersonalNumber(), employee.getPersonalNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
