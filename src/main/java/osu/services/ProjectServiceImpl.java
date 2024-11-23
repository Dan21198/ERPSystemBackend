package osu.services;

import jakarta.transaction.Transactional;
import osu.dto.ProjectDTO;
import osu.enums.ProjectStatus;
import osu.exception.RecordNotFoundException;
import osu.mapper.EmployeeMapper;
import osu.mapper.ProjectMapper;
import osu.model.Employee;
import osu.model.Project;
import osu.repository.EmployeeRepository;
import osu.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final osu.mapper.ProjectMapper projectMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                              ProjectMapper projectMapper, EmployeeMapper employeeMapper) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.projectMapper = projectMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);

        if (projectDTO.getEmployees() != null && !projectDTO.getEmployees().isEmpty()) {
            Set<Employee> employees = projectDTO.getEmployees().stream()
                    .map(employeeDTO -> employeeRepository.findById(employeeDTO.getId())
                            .orElseThrow(() -> new RecordNotFoundException("Employee with ID " +
                                    employeeDTO.getId() + " not found")))
                    .collect(Collectors.toSet());
            project.setEmployees(employees);
        }
        project.setEmployeeCount(project.getEmployees() == null ? 0 : project.getEmployees().size());
        Project savedProject = projectRepository.save(project);

        return projectMapper.toDto(savedProject);
    }




    @Override
    @Transactional
    public ProjectDTO updateProject(Long registrationNumber, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));

        updateProjectFromRequest(existingProject, projectDTO);
        Project updatedProject = projectRepository.save(existingProject);

        return projectMapper.toDto(updatedProject);
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
                .map(projectMapper::toDto);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    private void updateProjectFromRequest(Project project, ProjectDTO projectDTO) {
        Optional.ofNullable(projectDTO.getProjectEnd()).ifPresent(project::setProjectEnd);
        Optional.ofNullable(projectDTO.getProjectName()).ifPresent(project::setProjectName);
        Optional.ofNullable(projectDTO.getProjectCode()).ifPresent(project::setProjectCode);

        Optional.ofNullable(projectDTO.getProjectStatus())
                .map(ProjectStatus::valueOf)
                .ifPresent(project::setProjectStatus);

        Optional.ofNullable(projectDTO.getEmployees()).ifPresent(employeeDTOs -> {
            Set<Employee> newEmployees = employeeDTOs.stream()
                    .map(employeeMapper::toEntity)
                    .collect(Collectors.toSet());

            project.getEmployees().addAll(newEmployees);
        });

        project.setEmployeeCount(project.getEmployees().size());
    }
}