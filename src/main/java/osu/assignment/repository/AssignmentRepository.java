package osu.assignment.repository;

import osu.assignment.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.employee.model.Employee;
import osu.user.model.User;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    void deleteByEmployee(Employee existingEmployee);

    List<Assignment> findByCreatedBy(User authenticatedUser);
}