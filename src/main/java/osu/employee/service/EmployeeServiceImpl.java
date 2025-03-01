package osu.employee.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.employee.repository.EmployeeRepository;
import osu.exception.RecordNotFoundException;
import osu.employee.mapper.EmployeeMapper;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User authenticatedUser) {
        Employee employee = employeeMapper.toEntity(employeeDTO);

        if (employeeDTO.getPositionIds() != null) {
            Set<Position> positions = employeeDTO.getPositionIds().stream()
                    .map(positionId -> positionRepository.findById(positionId)
                            .orElseThrow(() -> new RuntimeException("Position with ID " + positionId + " not found")))
                    .collect(Collectors.toSet());
            employee.setPositions(positions);
        }

        employee.setCreatedBy(authenticatedUser);

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployee(Long personalNumber) {
        Employee employee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number " + personalNumber
                        + " not found"));
        return employeeMapper.toDto(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        try {
            employeeMapper.updateEmployeeFromDto(employeeDTO, existingEmployee);

            if (employeeDTO.getPositionIds() != null) {
                Set<Position> positions = employeeDTO.getPositionIds().stream()
                        .map(positionId -> positionRepository.findById(positionId)
                                .orElseThrow(() -> new RuntimeException("Position with ID " + positionId + " not found")))
                        .collect(Collectors.toSet());
                existingEmployee.setPositions(positions);
            }
            existingEmployee.calculateGrossSalary();

            Employee updatedEmployee = employeeRepository.save(existingEmployee);

            return employeeMapper.toDto(updatedEmployee);
        } catch (Exception e) {
            logger.error("Unexpected error while updating employee", e);
            throw new RuntimeException("Unexpected error updating employee", e);
        }
    }

    @Override
    public void deleteEmployee(Long personalNumber) {
        employeeRepository.deleteById(personalNumber);
    }

    @Override
    public List<EmployeeDTO> findEmployeesByName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            throw new IllegalArgumentException("At least one of firstName or lastName must be provided.");
        }

        List<Employee> employees = Optional.ofNullable(firstName)
                .flatMap(fn -> Optional.ofNullable(lastName)
                        .map(ln -> employeeRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(fn, ln))
                        .or(() -> Optional.of(employeeRepository.findByFirstNameIgnoreCaseContaining(fn))))
                .orElseGet(() -> employeeRepository.findByLastNameIgnoreCaseContaining(lastName));

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPositionName(String positionName) {
        List<Employee> employees = employeeRepository.findByPositions_NameIgnoreCase(positionName);
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> getEmployeesSortedBySalary(String order) {
        List<Employee> employees;
        if ("desc".equalsIgnoreCase(order)) {
            employees = employeeRepository.findAllByOrderByGrossSalaryDesc();
        } else {
            employees = employeeRepository.findAllByOrderByGrossSalaryAsc();
        }
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
}