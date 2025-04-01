package osu.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.employee.model.Employee;
import osu.user.model.User;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByCreatedBy(User authenticatedUser);

    List<Employee> findByCreatedByAndFirstNameContainingOrLastNameContaining(User authenticatedUser, String firstName, String lastName);

    List<Employee> findByCreatedByOrderByGrossSalaryDesc(User authenticatedUser);

    List<Employee> findByCreatedByOrderByGrossSalaryAsc(User authenticatedUser);
}