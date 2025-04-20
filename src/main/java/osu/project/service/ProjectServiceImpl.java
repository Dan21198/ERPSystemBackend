package osu.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.assignment.model.Assignment;
import osu.contract.model.Contract;
import osu.contract.repository.ContractRepository;
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
    private final ContractRepository contractRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, PositionRepository positionRepository,
                              ProjectMapper projectMapper, EmployeeMapper employeeMapper, ContractRepository contractRepository) {
        this.projectRepository = projectRepository;
        this.positionRepository = positionRepository;
        this.projectMapper = projectMapper;
        this.employeeMapper = employeeMapper;
        this.contractRepository = contractRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser) {
        validateProjectDates(projectDTO);

        Project project = projectMapper.toEntity(projectDTO);

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
        Project existingProject = getValidatedProject(projectId, authenticatedUser);

        validateProjectDates(projectDTO);

        try {
            projectMapper.updateProjectFromDto(projectDTO, existingProject);
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
        Project projectToDelete = getValidatedProject(projectId, authenticatedUser);
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
        Project project = getValidatedProject(projectId, authenticatedUser);

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
        Project project = getValidatedProject(projectId, authenticatedUser);
        Position position = getPositionById(positionId);

        project.getPositions().add(position);
        position.setProject(project);

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    @Transactional
    public ProjectDTO removePositionFromProject(Long projectId, Long positionId, User authenticatedUser) {
        Project project = getValidatedProject(projectId, authenticatedUser);
        Position position = getPositionById(positionId);

        project.getPositions().remove(position);
        position.setProject(null);

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    @Transactional
    public ProjectDTO addContractToProject(Long projectId, Long contractId, User authenticatedUser) {
        Project project = getValidatedProject(projectId, authenticatedUser);
        Contract contract = getContractById(contractId);

        contract.setProject(project);
        project.getContracts().add(contract);
        project.updateTotalAmountAllocated();

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
    }

    @Override
    @Transactional
    public ProjectDTO removeContractFromProject(Long projectId, Long contractId, User authenticatedUser) {
        Project project = getValidatedProject(projectId, authenticatedUser);
        Contract contract = getContractById(contractId);

        project.getContracts().remove(contract);
        contract.setProject(null);
        project.updateTotalAmountAllocated();

        Project updatedProject = projectRepository.save(project);
        return projectMapper.toDto(updatedProject);
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

    private Project getValidatedProject(Long projectId, User authenticatedUser) {
        User managedUser = entityManager.merge(authenticatedUser);
        Hibernate.initialize(managedUser.getProjects());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RecordNotFoundException("Project not found with id: " + projectId));

        if (!project.getUsers().contains(managedUser)) {
            throw new RecordNotFoundException("Unauthorized access to project");
        }
        return project;
    }

    private Position getPositionById(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RecordNotFoundException("Position not found with id: " + positionId));
    }

    private Contract getContractById(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new RecordNotFoundException("Contract not found with id: " + contractId));
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

    private void validateProjectDates(ProjectDTO projectDTO) {
        if (!Objects.equals(projectDTO.getProjectStatus(), ProjectStatus.SUSTAINABILITY.toString())) {
            if (projectDTO.getProjectStart() == null) {
                throw new IllegalArgumentException("Project start date is required for status: " + projectDTO.getProjectStatus());
            }
            if (projectDTO.getProjectEnd() == null) {
                throw new IllegalArgumentException("Project end date is required for status: " + projectDTO.getProjectStatus());
            }
        }
    }
}