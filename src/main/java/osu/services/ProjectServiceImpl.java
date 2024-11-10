package osu.services;

import osu.dto.ProjectDTO;
import osu.enums.ProjectStatus;
import osu.exception.RecordNotFoundException;
import osu.model.Employee;
import osu.model.Project;
import osu.repository.PositionRepository;
import osu.repository.ProjectRepository;
import osu.util.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PositionRepository positionRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = new ProjectMapper(positionRepository);
    }

    @Override
    public Project createProject(ProjectDTO projectDTO) {
        Project project = projectMapper.mapToProject(projectDTO);
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
                .map(projectMapper::mapToProjectResponse);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::mapToProjectResponse)
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
            Set<Employee> employees = employeeDTOs.stream()
                    .map(projectMapper::mapToEmployee)
                    .collect(Collectors.toSet());
            project.setEmployees(employees);
            project.setEmployeeCount(employees.size());
        });
    }
}