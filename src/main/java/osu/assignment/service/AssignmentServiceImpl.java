package osu.assignment.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import osu.assignment.model.AssignmentDTO;
import osu.assignment.mapper.AssignmentMapper;
import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;
import osu.position.repository.PositionRepository;
import osu.tariff.repository.TariffRepository;
import osu.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final TariffRepository tariffRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final EntityManager entityManager;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, AssignmentMapper assignmentMapper,
                                 TariffRepository tariffRepository, PositionRepository positionRepository,
                                 EmployeeRepository employeeRepository, EntityManager entityManager) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.tariffRepository = tariffRepository;
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

    @Override
    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO, User authenticatedUser) {
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);
        assignment.setCreatedBy(authenticatedUser);
        Assignment createdAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toDTO(createdAssignment);
    }

    @Override
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO, User authenticatedUser) {
        Assignment existingAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!existingAssignment.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only update assignments you created");
        }

        assignmentMapper.updateEntityFromDTO(assignmentDTO, existingAssignment);
        updateActiveStatusBasedOnDates(existingAssignment);

        Assignment updatedAssignment = assignmentRepository.save(existingAssignment);

        if (updatedAssignment.getEmployee() != null) {
            updatedAssignment.getEmployee().calculateGrossSalary();
            employeeRepository.save(updatedAssignment.getEmployee());
        }

        return assignmentMapper.toDTO(updatedAssignment);
    }

    private void updateActiveStatusBasedOnDates(Assignment existingAssignment) {
        LocalDate today = LocalDate.now();
        if (existingAssignment.getStartDate() != null && existingAssignment.getEndDate() != null) {
            existingAssignment.setActive(today.isAfter(existingAssignment.getStartDate())
                    && today.isBefore(existingAssignment.getEndDate()));
        } else {
            existingAssignment.setActive(false);
        }
    }

    @Override
    public void deleteAssignment(Long id, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only delete assignments you created");
        }

        assignmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AssignmentDTO getAssignmentById(Long id, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getCreatedBy().equals(managedUser)) {
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

        Assignment createdAssignment = assignmentRepository.save(assignment);

        assignment.getEmployee().calculateGrossSalary();
        employeeRepository.save(assignment.getEmployee());

        return assignmentMapper.toDTO(createdAssignment);
    }

    @Override
    public AssignmentDTO deactivateAssignment(Long id, User authenticatedUser) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You can only deactivate assignments you created");
        }

        assignment.setEndDate(LocalDate.now());
        assignment.setActive(false);
        Assignment updatedAssignment = assignmentRepository.save(assignment);

        Employee employee = assignment.getEmployee();
        employee.calculateGrossSalary();
        employeeRepository.save(employee);

        return assignmentMapper.toDTO(updatedAssignment);
    }
}