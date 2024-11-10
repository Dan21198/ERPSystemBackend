package osu.services;

import osu.dto.EmployeeRequest;
import osu.dto.ProjectRequest;
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

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository
            , PositionRepository positionRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    public Project createProject(ProjectRequest projectRequest) {
        Project project = new Project();
        project.setRegistrationNumber(projectRequest.getRegistrationNumber());
        project.setProjectCode(projectRequest.getProjectCode());
        project.setProjectName(projectRequest.getProjectName());
        project.setProjectStatus(ProjectStatus.valueOf(projectRequest.getProjectStatus()));  // Enum conversion
        project.setProjectStart(projectRequest.getProjectStart());
        project.setProjectEnd(projectRequest.getProjectEnd());

        Set<Employee> employees = new HashSet<>();
        for (EmployeeRequest employeeRequest : projectRequest.getEmployees()) {
            Employee employee = new Employee();
            employee.setPersonalNumber(employeeRequest.getPersonalNumber());
            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setTitle(AcademicTitle.valueOf(employeeRequest.getTitle()));  // Enum conversion
            employee.setContractStart(employeeRequest.getContractStart());
            employee.setContractEnd(employeeRequest.getContractEnd());
            employee.setWorkloadPercentage(employeeRequest.getWorkloadPercentage());
            employee.setSalaryGrade(employeeRequest.getSalaryGrade());
            employee.setTariffAmount(employeeRequest.getTariffAmount());
            employee.setPerformanceBonus(employeeRequest.getPerformanceBonus());
            employee.setGrossSalary(employeeRequest.getGrossSalary());

            Position position = positionRepository.findById(employeeRequest.getPosition().getId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            employee.setPosition(position);

            employees.add(employee);
        }

        project.setEmployees(employees);

        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long registrationNumber, Project projectDetails) {
        Project existingProject = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));

        if (projectDetails.getProjectEnd() != null) {
            existingProject.setProjectEnd(projectDetails.getProjectEnd());
        }

        if (projectDetails.getProjectName() != null) {
            existingProject.setProjectName(projectDetails.getProjectName());
        }

        if (projectDetails.getProjectCode() != null) {
            existingProject.setProjectCode(projectDetails.getProjectCode());
        }

        if (projectDetails.getProjectStatus() != null) {
            existingProject.setProjectStatus(projectDetails.getProjectStatus());
        }

        if (projectDetails.getEmployees() != null) {
            existingProject.setEmployees(projectDetails.getEmployees());

            existingProject.setEmployeeCount(projectDetails.getEmployees().size());
        }

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
    public Optional<Project> getProject(Long registrationNumber) {
        return projectRepository.findById(registrationNumber);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
