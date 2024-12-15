package osu.employee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.employee.repository.EmployeeRepository;
import osu.exception.RecordNotFoundException;
import osu.employee.mapper.EmployeeMapper;
import osu.position.mapper.PositionMapper;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;
    private final PositionMapper positionMapper;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                               EmployeeMapper employeeMapper, PositionMapper positionMapper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeMapper = employeeMapper;
        this.positionMapper = positionMapper;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);

        final Position position = employee.getPosition();
        if (position != null && position.getId() != null) {
            final Position foundPosition = positionRepository.findById(position.getId())
                    .orElseThrow(() -> new RuntimeException("Position with ID " + position.getId() + " not found"));
            employee.setPosition(foundPosition);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }


    @Override
    public EmployeeDTO getEmployee(Long personalNumber) {
        Employee employee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));
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
    public EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        try {
            employeeMapper.updateEmployeeFromDto(employeeDTO, existingEmployee);

            if (employeeDTO.getPosition() != null) {
                Position position = positionMapper.toEntity(employeeDTO.getPosition());
                existingEmployee.setPosition(position);
            }

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
                        .map(ln -> employeeRepository.
                                findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(fn, ln))
                        .or(() -> Optional.of(employeeRepository.findByFirstNameIgnoreCaseContaining(fn))))
                .orElseGet(() -> employeeRepository.findByLastNameIgnoreCaseContaining(lastName));

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPositionName(String positionName) {
        List<Employee> employees = employeeRepository.findByPosition_NameIgnoreCase(positionName);
        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

}