package osu.employee.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.employee.service.EmployeeService;
import osu.employee.model.EmployeeDTO;

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
    @Operation(summary = "Creates an employee", description = "Creates a new employee and returns the created employee's details")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployeeDTO = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployeeDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets an employee", description = "Retrieves the details of an employee by their ID")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.getEmployee(id);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all employees", description = "Retrieves a list of all employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an employee", description = "Updates the details of an existing employee by their ID")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id,
                                                      @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(id, employeeDTO);
        return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an employee", description = "Deletes an employee by their ID")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name-search")
    @Operation(summary = "Find employees by name", description = "Retrieves employees by their first and/or last name")
    public ResponseEntity<List<EmployeeDTO>> findEmployeesByName(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        List<EmployeeDTO> employees = employeeService.findEmployeesByName(firstName, lastName);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/position-search")
    @Operation(summary = "Find employees by position", description = "Retrieves employees by the name of their position")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPositionName(@RequestParam String positionName) {
        List<EmployeeDTO> employees = employeeService.findEmployeesByPositionName(positionName);
        return ResponseEntity.ok(employees);
    }
}
