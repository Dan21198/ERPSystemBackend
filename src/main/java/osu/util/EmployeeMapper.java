package osu.util;

import osu.dto.EmployeeDTO;
import osu.dto.PositionDTO;
import osu.enums.AcademicTitle;
import osu.model.Employee;
import osu.model.Position;
import osu.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final PositionRepository positionRepository;

    @Autowired
    public EmployeeMapper(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Employee mapToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setPersonalNumber(employeeDTO.getPersonalNumber());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getTitle() != null) {
            employee.setTitle(AcademicTitle.valueOf(employeeDTO.getTitle()));
        }
        employee.setContractStart(employeeDTO.getContractStart());
        employee.setContractEnd(employeeDTO.getContractEnd());
        employee.setWorkloadPercentage(employeeDTO.getWorkloadPercentage());
        employee.setSalaryGrade(employeeDTO.getSalaryGrade());
        employee.setTariffAmount(employeeDTO.getTariffAmount());
        employee.setPerformanceBonus(employeeDTO.getPerformanceBonus());
        employee.setGrossSalary(employeeDTO.getGrossSalary());

        if (employeeDTO.getPosition() != null && employeeDTO.getPosition().getId() != null) {
            Position position = positionRepository.findById(employeeDTO.getPosition().getId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            employee.setPosition(position);
        }

        return employee;
    }

    public EmployeeDTO mapToEmployeeResponse(Employee employee) {
        EmployeeDTO employeeResponse = new EmployeeDTO();
        employeeResponse.setPersonalNumber(employee.getPersonalNumber());
        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setTitle(employee.getTitle() != null ? employee.getTitle().toString() : null);
        employeeResponse.setContractStart(employee.getContractStart());
        employeeResponse.setContractEnd(employee.getContractEnd());
        employeeResponse.setWorkloadPercentage(employee.getWorkloadPercentage());
        employeeResponse.setSalaryGrade(employee.getSalaryGrade());
        employeeResponse.setTariffAmount(employee.getTariffAmount());
        employeeResponse.setPerformanceBonus(employee.getPerformanceBonus());
        employeeResponse.setGrossSalary(employee.getGrossSalary());

        if (employee.getPosition() != null) {
            PositionDTO positionDTO = mapToPositionDTO(employee.getPosition());
            employeeResponse.setPosition(positionDTO);
        }

        return employeeResponse;
    }

    public PositionDTO mapToPositionDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        return positionDTO;
    }
}