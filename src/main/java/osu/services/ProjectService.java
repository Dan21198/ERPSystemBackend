package osu.services;

import osu.dto.ProjectRequest;
import osu.model.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project createProject(ProjectRequest projectRequest);
    Project updateProject(Long registrationNumber, Project projectDetails);
    void deleteProject(Long registrationNumber);
    Optional<Project> getProject(Long registrationNumber);
    List<Project> getAllProjects();
}