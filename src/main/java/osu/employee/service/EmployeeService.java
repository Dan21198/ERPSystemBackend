package osu.employee.service;

import osu.employee.model.EmployeeDTO;
import osu.user.model.User;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO, User authenticatedUser);

    EmployeeDTO getEmployee(Long personalNumber, User authenticatedUser);

    List<EmployeeDTO> getAllEmployees(User authenticatedUser);

    EmployeeDTO updateEmployee(Long personalNumber, EmployeeDTO employeeDTO, User authenticatedUser);

    void deleteEmployee(Long personalNumber, User authenticatedUser);

    List<EmployeeDTO> findEmployeesByName(String firstName, String lastName, User authenticatedUser);

    List<EmployeeDTO> getEmployeesSortedBySalary(String order, User authenticatedUser);
}