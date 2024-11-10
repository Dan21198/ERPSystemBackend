package osu.controller;

import osu.model.Employee;
import osu.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{personalNumber}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long personalNumber) {
        Employee employee = employeeService.getEmployee(personalNumber);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/{personalNumber}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long personalNumber,
                                                   @RequestBody Employee employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(personalNumber, employeeDetails);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/{personalNumber}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long personalNumber) {
        employeeService.deleteEmployee(personalNumber);
        return ResponseEntity.noContent().build();
    }
}
