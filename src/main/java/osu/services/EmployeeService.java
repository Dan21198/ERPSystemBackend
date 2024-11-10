package osu.services;

import osu.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(Employee employee);

    Employee getEmployee(Long personalNumber);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long personalNumber, Employee employeeDetails);

    void deleteEmployee(Long personalNumber);
}