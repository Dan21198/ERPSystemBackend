package osu.services;

import jakarta.transaction.Transactional;
import osu.dto.EmployeeDTO;
import osu.enums.AcademicTitle;
import osu.exception.RecordNotFoundException;
import osu.model.Employee;
import osu.model.Position;
import osu.model.Project;
import osu.repository.EmployeeRepository;
import osu.repository.PositionRepository;
import osu.repository.ProjectRepository;
import osu.util.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper,
                               PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.mapToEmployee(employeeDTO);
        Employee createdEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToEmployeeResponse(createdEmployee);
    }

    @Override
    public EmployeeDTO getEmployee(Long personalNumber) {
        Employee employee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));
        return employeeMapper.mapToEmployeeResponse(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::mapToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));

        updateEmployeeDetails(existingEmployee, employeeDTO);

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.mapToEmployeeResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long personalNumber) {
        if (!employeeRepository.existsById(personalNumber)) {
            throw new RecordNotFoundException("Employee with personal number " + personalNumber + " not found");
        }
        employeeRepository.deleteById(personalNumber);
    }

    private void updateEmployeeDetails(Employee existingEmployee, EmployeeDTO employeeDTO) {
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

    public static void updateEmployeeDetails(Employee existingEmployee, Date contractStart, Date contractEnd,
                                             Double workloadPercentage, String salaryGrade, Double tariffAmount,
                                             Double performanceBonus, Double grossSalary) {
        existingEmployee.setContractStart(contractStart);
        existingEmployee.setContractEnd(contractEnd);
        existingEmployee.setWorkloadPercentage(workloadPercentage);
        existingEmployee.setSalaryGrade(salaryGrade);
        existingEmployee.setTariffAmount(tariffAmount);
        existingEmployee.setPerformanceBonus(performanceBonus);
        existingEmployee.setGrossSalary(grossSalary);
    }
}