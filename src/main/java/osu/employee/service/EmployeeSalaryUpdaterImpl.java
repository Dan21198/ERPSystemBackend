package osu.employee.service;

import osu.assignment.model.Assignment;
import osu.assignment.repository.AssignmentRepository;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class EmployeeSalaryUpdaterImpl implements EmployeeSalaryUpdater{
    private final AssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryCalculator salaryCalculator;

    public EmployeeSalaryUpdaterImpl(AssignmentRepository assignmentRepository,
                                     EmployeeRepository employeeRepository,
                                     SalaryCalculator salaryCalculator) {
        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.salaryCalculator = salaryCalculator;
    }

    @Transactional
    public void updateEmployeeSalary(Employee employee) {
        Set<Assignment> assignments = assignmentRepository.findByEmployee(employee);
        double newSalary = salaryCalculator.calculateGrossSalary(assignments);
        employee.setGrossSalary(newSalary);
        employeeRepository.save(employee);
    }
}