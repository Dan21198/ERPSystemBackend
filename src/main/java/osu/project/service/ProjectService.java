package osu.project.service;

import osu.employee.model.EmployeeDTO;
import osu.project.enums.ProjectStatus;
import osu.project.model.ProjectContractDTO;
import osu.project.model.ProjectDTO;
import osu.user.model.User;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser);

    ProjectDTO updateProject(Long registrationNumber, ProjectDTO projectDTO, User authenticatedUser);

    void deleteProject(Long registrationNumber, User authenticatedUser);

    Optional<ProjectDTO> getProject(Long registrationNumber, User authenticatedUser);

    List<ProjectDTO> getAllProjects(User authenticatedUser);

    Optional<ProjectContractDTO> getProjectWithContracts(Long projectId, User authenticatedUser);

    List<EmployeeDTO> getAllEmployeesOnProject(Long projectId, User authenticatedUser);

    List<ProjectDTO> getProjectsByProjectCode(String projectCode);

    List<ProjectDTO> getProjectsByProjectName(String projectName);

    List<ProjectDTO> getProjectsByProjectStatus(ProjectStatus projectStatus);

    List<ProjectDTO> getAllProjectsOrderedByStartDateAsc();

    List<ProjectDTO> getAllProjectsOrderedByStartDateDesc();

    List<ProjectDTO> getAllProjectsOrderedByEndDateAsc();

    List<ProjectDTO> getAllProjectsOrderedByEndDateDesc();

    ProjectDTO addPositionToProject(Long projectId, Long positionId, User authenticatedUser);

    ProjectDTO removePositionFromProject(Long projectId, Long positionId, User authenticatedUser);

    ProjectDTO addContractToProject(Long projectId, Long contractId, User authenticatedUser);

    ProjectDTO removeContractFromProject(Long projectId, Long contractId, User authenticatedUser);
}