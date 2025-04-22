package osu.performanceBonus.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import osu.employee.service.EmployeeSalaryUpdater;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.performanceBonus.mapper.PerformanceBonusMapper;
import osu.performanceBonus.repository.PerformanceBonusRepository;
import osu.exception.RecordNotFoundException;
import osu.position.repository.PositionRepository;
import osu.user.model.User;

@Service
public class PerformanceBonusServiceImpl implements PerformanceBonusService {
    private final AssignmentRepository assignmentRepository;
    private final PerformanceBonusRepository performanceBonusRepository;
    private final PerformanceBonusMapper performanceBonusMapper;
    private final EmployeeSalaryUpdater salaryUpdater;
    private final PositionRepository positionRepository;

    @Autowired
    public PerformanceBonusServiceImpl(AssignmentRepository assignmentRepository,
                                       PerformanceBonusRepository performanceBonusRepository,
                                       PerformanceBonusMapper performanceBonusMapper,
                                       EmployeeSalaryUpdater salaryUpdater,
                                       PositionRepository positionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.performanceBonusRepository = performanceBonusRepository;
        this.performanceBonusMapper = performanceBonusMapper;
        this.salaryUpdater = salaryUpdater;
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public PerformanceBonusDTO addBonus(Long assignmentId, PerformanceBonusDTO bonusDTO, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found"));

        verifyAssignmentAccess(assignment, authenticatedUser);

        PerformanceBonus bonus = performanceBonusMapper.toEntity(bonusDTO);
        bonus.setAssignment(assignment);
        bonus.setCreatedBy(authenticatedUser);
        PerformanceBonus savedBonus = performanceBonusRepository.save(bonus);

        updateEmployeeAndPosition(assignment);

        return performanceBonusMapper.toDto(savedBonus);
    }

    @Override
    @Transactional
    public void removeBonus(Long assignmentId, Long bonusId, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found"));

        verifyAssignmentAccess(assignment, authenticatedUser);

        PerformanceBonus bonus = performanceBonusRepository.findById(bonusId)
                .orElseThrow(() -> new RecordNotFoundException("Bonus not found"));

        if (!bonus.getAssignment().getId().equals(assignmentId)) {
            throw new IllegalArgumentException("Bonus does not belong to assignment");
        }

        if (!bonus.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only delete bonuses you created");
        }

        performanceBonusRepository.delete(bonus);
        updateEmployeeAndPosition(assignment);
    }

    @Override
    @Transactional
    public PerformanceBonusDTO updateBonus(Long assignmentId, Long bonusId, PerformanceBonusDTO bonusDTO, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found"));

        verifyAssignmentAccess(assignment, authenticatedUser);

        PerformanceBonus existingBonus = performanceBonusRepository.findByIdAndAssignmentId(bonusId, assignmentId)
                .orElseThrow(() -> new RecordNotFoundException("Bonus not found"));

        if (!existingBonus.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only update bonuses you created");
        }

        performanceBonusMapper.updateBonusFromDto(bonusDTO, existingBonus);
        PerformanceBonus updatedBonus = performanceBonusRepository.save(existingBonus);

        updateEmployeeAndPosition(assignment);

        return performanceBonusMapper.toDto(updatedBonus);
    }

    private void verifyAssignmentAccess(Assignment assignment, User authenticatedUser) {
        if (!assignment.getCreatedBy().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("You don't have access to this assignment");
        }
    }

    private void updateEmployeeAndPosition(Assignment assignment) {
        if (assignment.getEmployee() != null) {
            salaryUpdater.updateEmployeeSalary(assignment.getEmployee());
        }

        if (assignment.getPosition() != null) {
            assignment.getPosition().updateTotalAmountSpent();
            positionRepository.save(assignment.getPosition());
        }
    }
}