package osu.services;

import osu.dto.EmployeeDTO;
import osu.dto.PositionDTO;
import osu.dto.ProjectDTO;
import osu.enums.AcademicTitle;
import osu.enums.ProjectStatus;
import osu.exception.RecordNotFoundException;
import osu.model.Employee;
import osu.model.Position;
import osu.model.Project;
import osu.repository.EmployeeRepository;
import osu.repository.PositionRepository;
import osu.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                              PositionRepository positionRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Project project = mapToProject(projectDTO);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long registrationNumber, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));

        updateProjectFromRequest(existingProject, projectDTO);
        return projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long registrationNumber) {
        Project projectToDelete = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));

        projectRepository.delete(projectToDelete);
    }

    @Override
    public Optional<ProjectDTO> getProject(Long registrationNumber) {
        return projectRepository.findById(registrationNumber)
                .map(this::mapToProjectResponse);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    private void updateProjectFromRequest(Project project, ProjectDTO projectDTO) {
        Optional.ofNullable(projectDTO.getProjectEnd()).ifPresent(project::setProjectEnd);
        Optional.ofNullable(projectDTO.getProjectName()).ifPresent(project::setProjectName);
        Optional.ofNullable(projectDTO.getProjectCode()).ifPresent(project::setProjectCode);

        Optional.ofNullable(projectDTO.getProjectStatus())
                .map(ProjectStatus::valueOf)
                .ifPresent(project::setProjectStatus);

        if (projectDTO.getEmployees() == null) {
            System.out.println("projectDTO.getEmployees() is null");
        } else if (projectDTO.getEmployees().isEmpty()) {
            System.out.println("projectDTO.getEmployees() is empty");
        } else {
            System.out.println("projectDTO.getEmployees() is not null or empty");
        }

        Optional.ofNullable(projectDTO.getEmployees()).ifPresent(employeeDTOs -> {
            Set<Employee> employees = employeeDTOs.stream()
                    .map(this::mapToEmployee)
                    .collect(Collectors.toSet());
            project.setEmployees(employees);
            project.setEmployeeCount(employees.size());
            System.out.println("Employees: " + employees);
            System.out.println("Employee count: " + employees.size());
        });
    }

    private Project mapToProject(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setRegistrationNumber(projectDTO.getRegistrationNumber());
        project.setProjectCode(projectDTO.getProjectCode());
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectStatus(ProjectStatus.valueOf(projectDTO.getProjectStatus()));
        project.setProjectStart(projectDTO.getProjectStart());
        project.setProjectEnd(projectDTO.getProjectEnd());

        Set<Employee> employees = projectDTO.getEmployees().stream()
                .map(this::mapToEmployee)
                .collect(Collectors.toSet());
        project.setEmployees(employees);
        project.setEmployeeCount(employees.size());

        return project;
    }

    private ProjectDTO mapToProjectResponse(Project project) {
        ProjectDTO projectResponse = new ProjectDTO();
        projectResponse.setRegistrationNumber(project.getRegistrationNumber());
        projectResponse.setProjectCode(project.getProjectCode());
        projectResponse.setProjectName(project.getProjectName());
        projectResponse.setProjectStatus(project.getProjectStatus().toString());
        projectResponse.setProjectStart(project.getProjectStart());
        projectResponse.setProjectEnd(project.getProjectEnd());
        projectResponse.setEmployeeCount(project.getEmployeeCount());

        Set<EmployeeDTO> employeeResponses = project.getEmployees().stream()
                .map(this::mapToEmployeeResponse)
                .collect(Collectors.toSet());
        projectResponse.setEmployees(employeeResponses);

        return projectResponse;
    }

    private Employee mapToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setPersonalNumber(employeeDTO.getPersonalNumber());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setTitle(AcademicTitle.valueOf(employeeDTO.getTitle()));
        employee.setContractStart(employeeDTO.getContractStart());
        employee.setContractEnd(employeeDTO.getContractEnd());
        employee.setWorkloadPercentage(employeeDTO.getWorkloadPercentage());
        employee.setSalaryGrade(employeeDTO.getSalaryGrade());
        employee.setTariffAmount(employeeDTO.getTariffAmount());
        employee.setPerformanceBonus(employeeDTO.getPerformanceBonus());
        employee.setGrossSalary(employeeDTO.getGrossSalary());
        employee.setPosition(employee.getPosition());

        Position position = positionRepository.findById(employeeDTO.getPosition().getId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        employee.setPosition(position);

        return employee;
    }

    private EmployeeDTO mapToEmployeeResponse(Employee employee) {
        EmployeeDTO employeeResponse = new EmployeeDTO();
        employeeResponse.setPersonalNumber(employee.getPersonalNumber());
        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setTitle(employee.getTitle().toString());
        employeeResponse.setContractStart(employee.getContractStart());
        employeeResponse.setContractEnd(employee.getContractEnd());
        employeeResponse.setWorkloadPercentage(employee.getWorkloadPercentage());
        employeeResponse.setSalaryGrade(employee.getSalaryGrade());
        employeeResponse.setTariffAmount(employee.getTariffAmount());
        employeeResponse.setPerformanceBonus(employee.getPerformanceBonus());
        employeeResponse.setGrossSalary(employee.getGrossSalary());

        PositionDTO positionDTO = mapToPositionDTO(employee.getPosition());
        employeeResponse.setPosition(positionDTO);

        return employeeResponse;
    }

    private PositionDTO mapToPositionDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        return positionDTO;
    }

}
