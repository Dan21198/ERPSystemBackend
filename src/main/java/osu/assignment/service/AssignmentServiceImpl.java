package osu.assignment.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.AssignmentDTO;
import osu.assignment.mapper.AssignmentMapper;
import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import osu.employee.repository.EmployeeRepository;
import osu.employee.service.EmployeeSalaryUpdater;
import osu.exception.RecordNotFoundException;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
import osu.position.service.PositionCalculationService;
import osu.tariff.repository.TariffRepository;
import osu.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final TariffRepository tariffRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeSalaryUpdater salaryUpdater;
    private final EntityManager entityManager;
    private final PositionCalculationService positionCalculationService;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 AssignmentMapper assignmentMapper,
                                 TariffRepository tariffRepository,
                                 PositionRepository positionRepository,
                                 EmployeeRepository employeeRepository,
                                 EmployeeSalaryUpdater salaryUpdater,
                                 EntityManager entityManager, PositionCalculationService positionCalculationService) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.tariffRepository = tariffRepository;
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.salaryUpdater = salaryUpdater;
        this.entityManager = entityManager;
        this.positionCalculationService = positionCalculationService;
    }

    @Override
    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO, User authenticatedUser) {
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);
        assignment.setCreatedBy(authenticatedUser);
        Assignment createdAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toDTO(createdAssignment);
    }

    @Override
    @Transactional
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        Assignment existingAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!existingAssignment.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only update assignments you created");
        }

        assignmentMapper.updateEntityFromDTO(assignmentDTO, existingAssignment);
        updateActiveStatusBasedOnDates(existingAssignment);

        Assignment updatedAssignment = assignmentRepository.save(existingAssignment);

        if (updatedAssignment.getEmployee() != null) {
            salaryUpdater.updateEmployeeSalary(updatedAssignment.getEmployee());
        }

        return assignmentMapper.toDTO(updatedAssignment);
    }

    private void updateActiveStatusBasedOnDates(Assignment existingAssignment) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = existingAssignment.getStartDate();
        LocalDate endDate = existingAssignment.getEndDate();

        boolean isActive = areDatesValid(startDate, endDate) && isDateInRange(today, startDate, endDate);
        existingAssignment.setActive(isActive);
    }

    private boolean areDatesValid(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    private boolean isDateInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
        return !dateToCheck.isBefore(startDate) && !dateToCheck.isAfter(endDate);
    }

    @Override
    public void deleteAssignment(Long id, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only delete assignments you created");
        }

        if (assignment.getEmployee() != null) {
            salaryUpdater.updateEmployeeSalary(assignment.getEmployee());
        }

        assignmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AssignmentDTO getAssignmentById(Long id, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Assignment not found"));

        if (!Objects.equals(assignment.getCreatedBy().getId(), authenticatedUser.getId())) {
            throw new SecurityException("You can only view assignments you created");
        }

        return assignmentMapper.toDTO(assignment);
    }

    @Override
    @Transactional
    public List<AssignmentDTO> getAllAssignments(User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        List<Assignment> assignments = assignmentRepository.findByCreatedBy(managedUser);
        return assignments.stream()
                .map(assignmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssignmentDTO assignTariffAndEmployeeToPosition(AssignmentDTO assignmentDTO, User authenticatedUser) {
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO, employeeRepository, positionRepository,
                tariffRepository);
        assignment.setCreatedBy(authenticatedUser);

        assignment.setStartDate(assignmentDTO.getStartDate());
        assignment.setEndDate(assignmentDTO.getEndDate());
        assignment.setAllocatedTimePercentage(assignmentDTO.getAllocatedTimePercentage());
        assignment.setActive(true);

        return getAssignmentDTO(assignment);
    }

    @Override
    @Transactional
    public AssignmentDTO deactivateAssignment(Long id, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getCreatedBy().equals(managedUser)) {
            throw new SecurityException("You can only deactivate assignments you created");
        }

        assignment.setEndDate(LocalDate.now());
        assignment.setActive(false);
        return getAssignmentDTO(assignment);
    }


    private AssignmentDTO getAssignmentDTO(Assignment assignment) {
        Assignment createdAssignment = assignmentRepository.save(assignment);

        Position position = assignment.getPosition();
        positionCalculationService.updateTotalAmountSpent(position);
        positionRepository.save(position);

        if (assignment.getEmployee() != null) {
            salaryUpdater.updateEmployeeSalary(assignment.getEmployee());
        }

        return assignmentMapper.toDTO(createdAssignment);
    }

}