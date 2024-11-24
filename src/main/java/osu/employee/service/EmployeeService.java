package osu.employee.service;

import osu.employee.model.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO getEmployee(Long personalNumber);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO);

    void deleteEmployee(Long personalNumber);
}