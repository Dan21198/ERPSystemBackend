package osu.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String passwordHash;
    private String role;
    private Boolean isActive;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;

    @OneToMany(mappedBy = "createdBy")
    private Set<Employee> employees;
}
