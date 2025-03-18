package osu.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.employee.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByOrderByGrossSalaryAsc();

    List<Employee> findAllByOrderByGrossSalaryDesc();

    List<Employee> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}