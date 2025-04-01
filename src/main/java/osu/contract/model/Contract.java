package osu.contract.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import osu.project.model.Project;
import osu.user.model.User;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order number cannot be blank")
    @Size(max = 50, message = "Order number must not exceed 50 characters")
    private String orderNumber;

    @NotBlank(message = "Workplace number cannot be blank")
    @Size(max = 50, message = "Workplace number must not exceed 50 characters")
    private String workplaceNumber;

    @NotBlank(message = "Source cannot be blank")
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @NotBlank(message = "Type cannot be blank")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;

    @NotBlank(message = "Order name cannot be blank")
    @Size(max = 100, message = "Order name must not exceed 100 characters")
    private String orderName;

    @NotNull(message = "Available amount is required")
    @Positive(message = "Available amount must be positive")
    private Double availableAmount;

    @NotNull(message = "Duration start date is required")
    private Date durationFrom;

    @NotNull(message = "Duration end date is required")
    private Date durationTo;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @ToString.Exclude
    private Project project;

    @PrePersist
    @PreUpdate
    public void synchronizeOrderName() {
        if (this.project != null) {
            this.orderName = this.project.getProjectName();
        }
    }

    public void setProject(Project project) {
        this.project = project;
        synchronizeOrderName();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonManagedReference
    @ToString.Exclude
    private User createdBy;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Contract contract = (Contract) o;
        return getId() != null && Objects.equals(getId(), contract.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
