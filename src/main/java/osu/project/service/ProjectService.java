package osu.project.service;

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
}