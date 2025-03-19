package osu.assignment.service;

import jakarta.transaction.Transactional;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentMapper assignmentMapper;
    private final TariffRepository tariffRepository;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, AssignmentMapper assignmentMapper,
                                 TariffRepository tariffRepository, PositionRepository positionRepository,
                                 EmployeeRepository employeeRepository) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.tariffRepository = tariffRepository;
        this.positionRepository = positionRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AssignmentDTO createAssignment(AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);
        Assignment createdAssignment = assignmentRepository.save(assignment);
        return assignmentMapper.toDTO(createdAssignment);
    }

    @Override
    public AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO) {
        Assignment existingAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        assignmentMapper.updateEntityFromDTO(assignmentDTO, existingAssignment);

        Assignment updatedAssignment = assignmentRepository.save(existingAssignment);
        return assignmentMapper.toDTO(updatedAssignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        return assignmentMapper.toDTO(assignment);
    }

    @Override
    public List<AssignmentDTO> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return assignments.stream()
                .map(assignmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssignmentDTO assignTariffAndEmployeeToPosition(AssignmentDTO assignmentDTO) {
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO, employeeRepository, positionRepository,
                tariffRepository);

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
    public AssignmentDTO deactivateAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        assignment.setEndDate(java.time.LocalDate.now());
        assignment.setActive(false);
        Assignment updatedAssignment = assignmentRepository.save(assignment);

        Employee employee = assignment.getEmployee();
        assignment.getEmployee().calculateGrossSalary();
        employeeRepository.save(employee);

        return assignmentMapper.toDTO(updatedAssignment);
    }
}