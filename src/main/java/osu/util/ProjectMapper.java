package osu.util;

import osu.dto.EmployeeDTO;
import osu.dto.PositionDTO;
import osu.dto.ProjectDTO;
import osu.enums.AcademicTitle;
import osu.enums.ProjectStatus;
import osu.model.Employee;
import osu.model.Position;
import osu.model.Project;
import osu.repository.PositionRepository;

import java.util.Set;
import java.util.stream.Collectors;

public class ProjectMapper {

    private final PositionRepository positionRepository;

    public ProjectMapper(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Project mapToProject(ProjectDTO projectDTO) {
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

    public ProjectDTO mapToProjectResponse(Project project) {
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

    public Employee mapToEmployee(EmployeeDTO employeeDTO) {
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

    public EmployeeDTO mapToEmployeeResponse(Employee employee) {
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

    public PositionDTO mapToPositionDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        return positionDTO;
    }
}