package osu.services;

import osu.dto.EmployeeDTO;
import osu.enums.AcademicTitle;
import osu.exception.RecordNotFoundException;
import osu.mapper.EmployeeMapper;
import osu.model.Employee;
import osu.model.Position;
import osu.repository.EmployeeRepository;
import osu.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
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
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));

        updateEmployeeDetails(existingEmployee, employeeDTO);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long personalNumber) {
        employeeRepository.deleteById(personalNumber);
    }

    public void updateEmployeeDetails(Employee existingEmployee, EmployeeDTO employeeDTO) {
        if (employeeDTO.getFirstName() != null) {
            existingEmployee.setFirstName(employeeDTO.getFirstName());
        }
        if (employeeDTO.getLastName() != null) {
            existingEmployee.setLastName(employeeDTO.getLastName());
        }
        if (employeeDTO.getTitle() != null) {
            existingEmployee.setTitle(AcademicTitle.valueOf(employeeDTO.getTitle()));
        }
        if (employeeDTO.getContractStart() != null) {
            existingEmployee.setContractStart(employeeDTO.getContractStart());
        }
        if (employeeDTO.getContractEnd() != null) {
            existingEmployee.setContractEnd(employeeDTO.getContractEnd());
        }
        if (employeeDTO.getWorkloadPercentage() != null) {
            existingEmployee.setWorkloadPercentage(employeeDTO.getWorkloadPercentage());
        }
        if (employeeDTO.getSalaryGrade() != null) {
            existingEmployee.setSalaryGrade(employeeDTO.getSalaryGrade());
        }
        if (employeeDTO.getTariffAmount() != null) {
            existingEmployee.setTariffAmount(employeeDTO.getTariffAmount());
        }
        if (employeeDTO.getPerformanceBonus() != null) {
            existingEmployee.setPerformanceBonus(employeeDTO.getPerformanceBonus());
        }
        if (employeeDTO.getGrossSalary() != null) {
            existingEmployee.setGrossSalary(employeeDTO.getGrossSalary());
        }
        if (employeeDTO.getPosition() != null && employeeDTO.getPosition().getId() != null) {
            Position position = positionRepository.findById(employeeDTO.getPosition().getId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            existingEmployee.setPosition(position);
        }
    }
}