package osu.project.service;

import osu.project.enums.ProjectStatus;
import osu.project.model.ProjectDTO;
import osu.user.model.User;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    ProjectDTO createProject(ProjectDTO projectDTO, User authenticatedUser);
    ProjectDTO updateProject(Long registrationNumber, ProjectDTO projectDTO);
    void deleteProject(Long registrationNumber);
    Optional<ProjectDTO> getProject(Long registrationNumber);
    List<ProjectDTO> getAllProjects();
    List<ProjectDTO> getProjectsByProjectCode(String projectCode);
    List<ProjectDTO> getProjectsByProjectName(String projectName);
    List<ProjectDTO> getProjectsByProjectStatus(ProjectStatus projectStatus);
    List<ProjectDTO> getAllProjectsOrderedByStartDateAsc();
    List<ProjectDTO> getAllProjectsOrderedByStartDateDesc();
    List<ProjectDTO> getAllProjectsOrderedByEndDateAsc();
    List<ProjectDTO> getAllProjectsOrderedByEndDateDesc();
}