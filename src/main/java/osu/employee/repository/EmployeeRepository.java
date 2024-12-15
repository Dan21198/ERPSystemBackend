package osu.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.employee.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByFirstNameIgnoreCaseContaining(String firstName);

    List<Employee> findByLastNameIgnoreCaseContaining(String lastName);

    List<Employee> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(String firstName, String lastName);

    List<Employee> findByPosition_NameIgnoreCase(String positionName);

}