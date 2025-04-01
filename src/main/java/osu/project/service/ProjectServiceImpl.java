package osu.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.assignment.model.Assignment;
import osu.employee.mapper.EmployeeMapper;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.exception.RecordNotFoundException;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
import osu.project.enums.ProjectStatus;
import osu.project.mapper.ProjectMapper;
import osu.project.model.Project;
import osu.project.model.ProjectContractDTO;
import osu.project.model.ProjectDTO;
import osu.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PositionRepository positionRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeMapper employeeMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PositionRepository positionRepository,
                              ProjectMapper projectMapper, EmployeeMapper employeeMapper) {
        this.projectRepository = projectRepository;
        this.positionRepository = positionRepository;
        this.projectMapper = projectMapper;
        this.employeeMapper = employeeMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser) {
        Project project = projectMapper.toEntity(projectDTO);
        project.setProjectStatus(determineProjectStatus(project.getProjectStart(), project.getProjectEnd()));

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
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO, User authenticatedUser) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "Project with ID " + projectId + " not found"));

        if (isProjectOwnedByUser(existingProject, authenticatedUser)) {
            throw new RecordNotFoundException("Project not found or unauthorized access");
        }

        try {
            projectMapper.updateProjectFromDto(projectDTO, existingProject);
            existingProject.setProjectStatus(determineProjectStatus(existingProject.getProjectStart(),
                    existingProject.getProjectEnd()));

            fetchAndSetPositions(projectDTO, existingProject);

            Project updatedProject = projectRepository.save(existingProject);
            return projectMapper.toDto(updatedProject);
        } catch (Exception e) {
            logger.error("Unexpected error while updating project", e);
            throw new RuntimeException("Unexpected error updating project", e);
        }
    }

    @Override
    public void deleteProject(Long projectId, User authenticatedUser) {
        Project projectToDelete = projectRepository.findById(projectId)
                .orElseThrow(() -> new RecordNotFoundException("Project with ID " + projectId + " not found"));

        if (isProjectOwnedByUser(projectToDelete, authenticatedUser)) {
            throw new RecordNotFoundException("Project not found or unauthorized access");
        }

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
    @Transactional
    public List<EmployeeDTO> getAllEmployeesOnProject(Long projectId, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RecordNotFoundException("Project not found with id: " + projectId));

        Hibernate.initialize(managedUser.getProjects());

        if (!managedUser.getProjects().contains(project)) {
            throw new RecordNotFoundException("Unauthorized access to project");
        }

        Set<Position> positions = project.getPositions();

        Set<Employee> employees = positions.stream()
                .flatMap(position -> position.getAssignments().stream())
                .map(Assignment::getEmployee)
                .collect(Collectors.toSet());

        return employees.stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<ProjectContractDTO> getProjectWithContracts(Long projectId, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        return projectRepository.findById(projectId)
                .filter(project -> project.getUsers().contains(managedUser))
                .map(projectMapper::toProjectContractDto);
    }

    @Override
    @Transactional
    public ProjectDTO addPositionToProject(Long projectId, Long positionId, User authenticatedUser) {
        Map<String, Object> result = validateAndGetProjectAndPosition(projectId, positionId, authenticatedUser);
        Project project = (Project) result.get("project");
        Position position = (Position) result.get("position");

        project.getPositions().add(position);
        position.setProject(project);

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    @Transactional
    public ProjectDTO removePositionFromProject(Long projectId, Long positionId, User authenticatedUser) {
        Map<String, Object> result = validateAndGetProjectAndPosition(projectId, positionId, authenticatedUser);
        Project project = (Project) result.get("project");
        Position position = (Position) result.get("position");

        project.getPositions().remove(position);
        position.setProject(null);

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    private Map<String, Object> validateAndGetProjectAndPosition(Long projectId, Long positionId, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RecordNotFoundException("Project not found with id: " + projectId));

        if (!managedUser.getProjects().contains(project)) {
            throw new RecordNotFoundException("Unauthorized access to project");
        }

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RecordNotFoundException("Position not found with id: " + positionId));

        Map<String, Object> result = new HashMap<>();
        result.put("project", project);
        result.put("position", position);
        return result;
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

    private ProjectStatus determineProjectStatus(Date projectStart, Date projectEnd) {
        Date currentDate = new Date();
        if (currentDate.before(projectStart)) {
            return ProjectStatus.NOT_STARTED;
        } else if (currentDate.after(projectStart) && currentDate.before(projectEnd)) {
            return ProjectStatus.IN_PROGRESS;
        } else {
            return ProjectStatus.COMPLETED;
        }
    }

    private boolean isProjectOwnedByUser(Project project, User user) {
        User managedUser = entityManager.merge(user);
        Hibernate.initialize(managedUser.getProjects());
        return !project.getUsers().contains(managedUser);
    }
}