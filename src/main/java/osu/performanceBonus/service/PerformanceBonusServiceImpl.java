package osu.performanceBonus.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import osu.employee.repository.EmployeeRepository;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.performanceBonus.mapper.PerformanceBonusMapper;
import osu.performanceBonus.repository.PerformanceBonusRepository;
import osu.exception.RecordNotFoundException;

@Service
public class PerformanceBonusServiceImpl implements PerformanceBonusService {

    private final AssignmentRepository assignmentRepository;
    private final PerformanceBonusRepository performanceBonusRepository;
    private final PerformanceBonusMapper performanceBonusMapper;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PerformanceBonusServiceImpl(AssignmentRepository assignmentRepository,
                                       PerformanceBonusRepository performanceBonusRepository,
                                       PerformanceBonusMapper performanceBonusMapper,
                                       EmployeeRepository employeeRepository) {
        this.assignmentRepository = assignmentRepository;
        this.performanceBonusRepository = performanceBonusRepository;
        this.performanceBonusMapper = performanceBonusMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public PerformanceBonusDTO addBonus(Long assignmentId, PerformanceBonusDTO bonusDTO) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found with id: " + assignmentId));

        PerformanceBonus bonus = performanceBonusMapper.toEntity(bonusDTO);
        bonus.setAssignment(assignment);
        PerformanceBonus savedBonus = performanceBonusRepository.save(bonus);

        updateEmployeeSalaryIfNeeded(assignment);

        return performanceBonusMapper.toDto(savedBonus);
    }

    @Override
    @Transactional
    public void removeBonus(Long assignmentId, Long bonusId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found with id: " + assignmentId));

        PerformanceBonus bonus = performanceBonusRepository.findById(bonusId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Bonus with id %s not found", bonusId)));

        if (!bonus.getAssignment().getId().equals(assignmentId)) {
            throw new IllegalArgumentException(
                    String.format("Bonus %s does not belong to assignment %s", bonusId, assignmentId));
        }

        if (assignment.getPerformanceBonuses() != null) {
            assignment.getPerformanceBonuses().remove(bonus);
        }

        performanceBonusRepository.delete(bonus);

        updateEmployeeSalaryIfNeeded(assignment);
    }

    @Override
    @Transactional
    public PerformanceBonusDTO updateBonus(Long assignmentId, Long bonusId, PerformanceBonusDTO bonusDTO) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found with id: " + assignmentId));

        PerformanceBonus existingBonus = performanceBonusRepository.findByIdAndAssignmentId(bonusId, assignmentId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Bonus with id %s not found for assignment %s", bonusId, assignmentId)));

        performanceBonusMapper.updateBonusFromDto(bonusDTO, existingBonus);
        PerformanceBonus updatedBonus = performanceBonusRepository.save(existingBonus);

        updateEmployeeSalaryIfNeeded(assignment);

        return performanceBonusMapper.toDto(updatedBonus);
    }

    private void updateEmployeeSalaryIfNeeded(Assignment assignment) {
        if (assignment.getEmployee() != null) {
            assignment.getEmployee().calculateGrossSalary();
            employeeRepository.save(assignment.getEmployee());
        }
    }
}