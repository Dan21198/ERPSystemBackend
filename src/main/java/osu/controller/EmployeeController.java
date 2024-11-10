package osu.controller;

import osu.dto.EmployeeDTO;
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
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployeeDTO = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployeeDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{personalNumber}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long personalNumber) {
        EmployeeDTO employeeDTO = employeeService.getEmployee(personalNumber);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();
        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }

    @PutMapping("/{personalNumber}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long personalNumber,
                                                      @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(personalNumber, employeeDTO);
        return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{personalNumber}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long personalNumber) {
        employeeService.deleteEmployee(personalNumber);
        return ResponseEntity.noContent().build();
    }
}