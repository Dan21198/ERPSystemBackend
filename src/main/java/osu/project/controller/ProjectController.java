package osu.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.project.service.ProjectService;
import osu.project.model.ProjectDTO;
import osu.user.model.User;
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
    @Operation(summary = "Creates a project", description = "Creates a new project")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        ProjectDTO projectResponse = projectService.createProject(projectRequest, authenticatedUser);

        return new ResponseEntity<>(projectResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a project", description = "Updates an existing project by ID")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTO projectRequest) {
        ProjectDTO updatedProjectDTO = projectService.updateProject(id, projectRequest);
        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a project", description = "Retrieves a project by its ID")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        ProjectDTO projectResponse = projectService.getProject(id)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + id + " not found"));
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all projects", description = "Retrieves all projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projectResponses = projectService.getAllProjects();
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }
}
