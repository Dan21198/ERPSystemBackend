package osu.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import osu.exception.RecordNotFoundException;
import osu.employee.mapper.EmployeeMapper;
import osu.employee.model.Employee;
import osu.project.enums.ProjectStatus;
import osu.project.mapper.ProjectMapper;
import osu.project.model.Project;
import osu.project.model.ProjectDTO;
import osu.project.repository.ProjectRepository;
import osu.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository,
                              ProjectMapper projectMapper, EmployeeMapper employeeMapper) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.projectMapper = projectMapper;
        this.employeeMapper = employeeMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser) {
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

        if (project.getUsers() == null) {
            project.setUsers(new HashSet<>());
        }

        User managedUser = entityManager.merge(authenticatedUser);
        project.getUsers().add(managedUser);

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