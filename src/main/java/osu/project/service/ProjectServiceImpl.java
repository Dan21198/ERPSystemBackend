package osu.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.exception.RecordNotFoundException;
import osu.employee.model.Employee;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
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
    private final PositionRepository positionRepository;
    private final ProjectMapper projectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository, PositionRepository positionRepository,
                              ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.projectMapper = projectMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser) {
        Project project = projectMapper.toEntity(projectDTO);

        fetchAndSetEmployees(projectDTO, project);
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
    public ProjectDTO updateProject(Long registrationNumber, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Project with registration number " + registrationNumber + " not found"));

        try {
            projectMapper.updateProjectFromDto(projectDTO, existingProject);

            fetchAndSetPositions(projectDTO, existingProject);
            fetchAndSetEmployees(projectDTO, existingProject);

            existingProject.setEmployeeCount(
                    Optional.ofNullable(existingProject.getEmployees())
                            .map(Set::size)
                            .orElse(0)
            );

            Project updatedProject = projectRepository.save(existingProject);

            return projectMapper.toDto(updatedProject);
        } catch (Exception e) {
            logger.error("Unexpected error while updating project", e);
            throw new RuntimeException("Unexpected error updating project", e);
        }
    }

    @Override
    public void deleteProject(Long registrationNumber) {
        Project projectToDelete = projectRepository.findById(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));

        projectRepository.delete(projectToDelete);
    }

    @Override
    @Transactional
    public Optional<ProjectDTO> getProject(Long registrationNumber, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        return projectRepository.findById(registrationNumber)
                .filter(project -> project.getUsers().contains(managedUser))
                .map(projectMapper::toDto);
    }

    @Override
    public List<ProjectDTO> getAllProjects(User authenticatedUser) {
        return projectRepository.findByUsersContaining(authenticatedUser)
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByProjectCode(String projectCode) {
        return projectRepository.findByProjectCodeIgnoreCase(projectCode).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByProjectName(String projectName) {
        return projectRepository.findByProjectNameIgnoreCaseContaining(projectName).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getProjectsByProjectStatus(ProjectStatus projectStatus) {
        return projectRepository.findByProjectStatus(projectStatus).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getAllProjectsOrderedByStartDateAsc() {
        return projectRepository.findAllByOrderByProjectStartAsc()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getAllProjectsOrderedByStartDateDesc() {
        return projectRepository.findAllByOrderByProjectStartDesc()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getAllProjectsOrderedByEndDateAsc() {
        return projectRepository.findAllByOrderByProjectEndAsc()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> getAllProjectsOrderedByEndDateDesc() {
        return projectRepository.findAllByOrderByProjectEndDesc()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    private void fetchAndSetEmployees(ProjectDTO projectDTO, Project existingProject) {
        if (projectDTO.getEmployees() != null && !projectDTO.getEmployees().isEmpty()) {
            Set<Employee> newEmployees = projectDTO.getEmployees().stream()
                    .map(employeeDTO -> employeeRepository.findById(employeeDTO.getId())
                            .orElseThrow(() -> new RecordNotFoundException(
                                    "Employee with ID " + employeeDTO.getId() + " not found")))
                    .collect(Collectors.toSet());
            existingProject.setEmployees(newEmployees);
        }
    }

    private void fetchAndSetPositions(ProjectDTO projectDTO, Project existingProject) {
        if (projectDTO.getPositions() != null && !projectDTO.getPositions().isEmpty()) {
            Set<Position> newPositions = projectDTO.getPositions().stream()
                    .map(positionDTO -> positionRepository.findById(positionDTO.getId())
                            .orElseThrow(() -> new RecordNotFoundException(
                                    "Position with ID " + positionDTO.getId() + " not found")))
                    .collect(Collectors.toSet());
            existingProject.setPositions(newPositions);
        }
    }
}