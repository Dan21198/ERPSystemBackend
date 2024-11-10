package osu.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import osu.exception.RecordNotFoundException;
import osu.model.Employee;
import osu.model.Position;
import osu.model.Project;
import osu.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.repository.PositionRepository;
import osu.repository.ProjectRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository
            , ProjectRepository projectRepository) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employee.getPosition() != null && employee.getPosition().getId() != null) {
            Position position = positionRepository.findById(employee.getPosition().getId())
                    .orElseGet(() -> {
                        Position newPosition = new Position();
                        newPosition.setId(employee.getPosition().getId());
                        newPosition.setName(employee.getPosition().getName());
                        return positionRepository.save(newPosition);
                    });

            employee.setPosition(position);
        }

        return employeeRepository.save(employee);
    }


    @Override
    public Employee getEmployee(Long personalNumber) {
        return employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Long personalNumber, Employee employeeDetails) {
        Employee existingEmployee = employeeRepository.findById(personalNumber)
                .orElseThrow(() -> new RecordNotFoundException("Employee with personal number "
                        + personalNumber + " not found"));

        BeanUtils.copyProperties(employeeDetails, existingEmployee, "personalNumber");

        Optional.ofNullable(employeeDetails.getPosition())
                .ifPresent(existingEmployee::setPosition);

        Optional.ofNullable(employeeDetails.getProjects())
                .ifPresent(projects -> {
                    Set<Project> updatedProjects = new HashSet<>();
                    for (Project project : projects) {
                        Project existingProject = projectRepository.findById(project.getRegistrationNumber())
                                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                                        + project.getRegistrationNumber() + " not found"));
                        updatedProjects.add(existingProject);
                    }
                    existingEmployee.setProjects(updatedProjects);
                });

        // Save the updated employee
        return employeeRepository.save(existingEmployee);
    }



    @Override
    public void deleteEmployee(Long personalNumber) {
        if (!employeeRepository.existsById(personalNumber)) {
            throw new RecordNotFoundException("Employee with personal number " + personalNumber + " not found");
        }
        employeeRepository.deleteById(personalNumber);
    }
}