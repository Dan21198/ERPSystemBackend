package osu.employee.service;

import osu.employee.model.EmployeeDTO;
import osu.user.model.User;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User authenticatedUser);

    EmployeeDTO getEmployee(Long personalNumber);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO);

    void deleteEmployee(Long personalNumber);

    List<EmployeeDTO> findEmployeesByName(String firstName, String lastName);

    List<EmployeeDTO> findEmployeesByPositionName(String positionName);

    List<EmployeeDTO> getEmployeesSortedBySalary(String order);
}