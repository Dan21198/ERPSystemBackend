package osu.assignment.repository;

import osu.assignment.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.employee.model.Employee;
import osu.position.model.Position;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    void deleteByEmployee(Employee existingEmployee);

    void deleteByPosition(Position positionToDelete);
}