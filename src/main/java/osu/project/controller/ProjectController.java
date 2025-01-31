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
import osu.project.enums.ProjectStatus;
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
            @RequestBody ProjectDTO projectRequest) {
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
    @Operation(summary = "Gets a project for the authenticated user",
            description = "Retrieves a project by its ID if the user has access")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        ProjectDTO projectResponse = projectService.getProject(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Project not found or unauthorized access"));

        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all projects for the authenticated user",
            description = "Retrieves all projects created by the authenticated user")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        List<ProjectDTO> projectResponses = projectService.getAllProjects(authenticatedUser);
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }

    @GetMapping("/code/{projectCode}")
    @Operation(summary = "Gets projects by project code", description = "Retrieves projects by project code")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectCode(@PathVariable String projectCode) {
        List<ProjectDTO> projects = projectService.getProjectsByProjectCode(projectCode);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/name/{projectName}")
    @Operation(summary = "Gets projects by name", description = "Retrieves projects containing a given name")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectName(@PathVariable String projectName) {
        List<ProjectDTO> projects = projectService.getProjectsByProjectName(projectName);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/status/{projectStatus}")
    @Operation(summary = "Gets projects by status", description = "Retrieves projects by project status")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectStatus(@PathVariable String projectStatus) {
        ProjectStatus statusEnum = ProjectStatus.valueOf(projectStatus.toUpperCase()); // Convert string to enum
        List<ProjectDTO> projects = projectService.getProjectsByProjectStatus(statusEnum);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/ordered/start-date/asc")
    @Operation(summary = "Gets projects ordered by start date ascending",
            description = "Retrieves all projects ordered by start date in ascending order")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsOrderedByStartDateAsc() {
        return ResponseEntity.ok(projectService.getAllProjectsOrderedByStartDateAsc());
    }

    @GetMapping("/ordered/start-date/desc")
    @Operation(summary = "Gets projects ordered by start date descending",
            description = "Retrieves all projects ordered by start date in descending order")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsOrderedByStartDateDesc() {
        return ResponseEntity.ok(projectService.getAllProjectsOrderedByStartDateDesc());
    }

    @GetMapping("/ordered/end-date/asc")
    @Operation(summary = "Gets projects ordered by end date ascending",
            description = "Retrieves all projects ordered by end date in ascending order")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsOrderedByEndDateAsc() {
        return ResponseEntity.ok(projectService.getAllProjectsOrderedByEndDateAsc());
    }

    @GetMapping("/ordered/end-date/desc")
    @Operation(summary = "Gets projects ordered by end date descending",
            description = "Retrieves all projects ordered by end date in descending order")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsOrderedByEndDateDesc() {
        return ResponseEntity.ok(projectService.getAllProjectsOrderedByEndDateDesc());
    }

}
