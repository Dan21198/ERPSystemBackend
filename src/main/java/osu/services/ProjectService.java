package osu.services;

import osu.dto.ProjectDTO;
import osu.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project createProject(ProjectDTO projectDTO);
    Project updateProject(Long registrationNumber, ProjectDTO projectDTO);
    void deleteProject(Long registrationNumber);
    Optional<ProjectDTO> getProject(Long registrationNumber);
    List<ProjectDTO> getAllProjects();
}