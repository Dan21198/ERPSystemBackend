package osu.employee.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.employee.mapper.EmployeeMapper;
import osu.employee.repository.EmployeeRepository;
import osu.exception.RecordNotFoundException;
import osu.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AssignmentRepository assignmentRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeSalaryUpdater salaryUpdater;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               AssignmentRepository assignmentRepository,
                               EmployeeMapper employeeMapper,
                               EmployeeSalaryUpdater salaryUpdater) {
        this.employeeRepository = employeeRepository;
        this.assignmentRepository = assignmentRepository;
        this.employeeMapper = employeeMapper;
        this.salaryUpdater = salaryUpdater;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User authenticatedUser) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setCreatedBy(authenticatedUser);
        Employee savedEmployee = employeeRepository.save(employee);

        if (employeeDTO.getAssignments() != null) {
            Set<Assignment> assignments = employeeDTO.getAssignments().stream()
                    .map(assignmentDTO -> {
                        Assignment assignment = employeeMapper.toAssignmentEntity(assignmentDTO);
                        assignment.setEmployee(savedEmployee);
                        return assignment;
                    })
                    .collect(Collectors.toSet());
            assignmentRepository.saveAll(assignments);

            salaryUpdater.updateEmployeeSalary(savedEmployee);
        }

        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO getEmployee(Long personalNumber, User authenticatedUser) {
        Employee employee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        if (!Objects.equals(employee.getCreatedBy().getId(), authenticatedUser.getId())) {
            throw new SecurityException("Unauthorized access to employee data");
        }

        return employeeMapper.toDto(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees(User authenticatedUser) {
        List<Employee> employees = employeeRepository.findByCreatedBy(authenticatedUser);

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO, User authenticatedUser) {
        Employee existingEmployee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        if (!existingEmployee.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You don't have permission to update this employee");
        }

        try {
            employeeMapper.updateEmployeeFromDto(employeeDTO, existingEmployee);

            if (employeeDTO.getAssignments() != null) {
                assignmentRepository.deleteByEmployee(existingEmployee);

                Set<Assignment> assignments = employeeDTO.getAssignments().stream()
                        .map(assignmentDTO -> {
                            Assignment assignment = employeeMapper.toAssignmentEntity(assignmentDTO);
                            assignment.setEmployee(existingEmployee);
                            return assignment;
                        })
                        .collect(Collectors.toSet());
                assignmentRepository.saveAll(assignments);
            }

            salaryUpdater.updateEmployeeSalary(existingEmployee);
            return employeeMapper.toDto(existingEmployee);
        } catch (Exception e) {
            logger.error("Unexpected error while updating employee", e);
            throw new RuntimeException("Unexpected error updating employee", e);
        }
    }

    @Override
    public void deleteEmployee(Long personalNumber, User authenticatedUser) {
        Employee employee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        if (!employee.getCreatedBy().equals(authenticatedUser)) {
            throw new SecurityException("You don't have permission to delete this employee");
        }

        employeeRepository.deleteById(personalNumber);
    }

    @Override
    public List<EmployeeDTO> findEmployeesByName(String firstName, String lastName, User authenticatedUser) {
        if (firstName == null && lastName == null) {
            throw new IllegalArgumentException("At least one of firstName or lastName must be provided.");
        }

        List<Employee> employees = employeeRepository
                .findByCreatedByAndFirstNameContainingOrLastNameContaining(
                        authenticatedUser,
                        firstName,
                        lastName
                );

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesSortedBySalary(String order, User authenticatedUser) {
        List<Employee> employees;
        if ("desc".equalsIgnoreCase(order)) {
            employees = employeeRepository.findByCreatedByOrderByGrossSalaryDesc(authenticatedUser);
        } else {
            employees = employeeRepository.findByCreatedByOrderByGrossSalaryAsc(authenticatedUser);
        }
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
}