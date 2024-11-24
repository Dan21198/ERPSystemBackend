package osu.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.project.service.ProjectService;
import osu.project.model.ProjectDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create a project", description = "Creates a new project")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectRequest) {
        ProjectDTO projectResponse = projectService.createProject(projectRequest);
        return new ResponseEntity<>(projectResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates an existing project by ID")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectRequest) {
        ProjectDTO updatedProjectDTO = projectService.updateProject(id, projectRequest);
        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project", description = "Retrieves a project by its ID")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        ProjectDTO projectResponse = projectService.getProject(id)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + id + " not found"));
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projectResponses = projectService.getAllProjects();
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }
}
