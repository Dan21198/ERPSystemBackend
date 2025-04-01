package osu.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import osu.employee.model.EmployeeDTO;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import osu.project.enums.ProjectStatus;
import osu.project.model.ProjectContractDTO;
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
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody ProjectDTO projectRequest,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectDTO projectResponse = projectService.createProject(projectRequest, authenticatedUser);
        return new ResponseEntity<>(projectResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a project", description = "Updates an existing project by ID")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectDTO projectRequest,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectDTO updatedProjectDTO = projectService.updateProject(id, projectRequest, authenticatedUser);
        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        projectService.deleteProject(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a project", description = "Retrieves a project by its ID")
    public ResponseEntity<ProjectDTO> getProject(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectDTO projectResponse = projectService.getProject(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Project not found"));
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all projects", description = "Retrieves all projects the user has access to")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @AuthenticationPrincipal User authenticatedUser) {
        List<ProjectDTO> projectResponses = projectService.getAllProjects(authenticatedUser);
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}/contracts")
    @Operation(summary = "Gets a project with contracts",
            description = "Retrieves a project with all its contracts")
    public ResponseEntity<ProjectContractDTO> getProjectWithContracts(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectContractDTO projectContractDTO = projectService.getProjectWithContracts(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Project not found"));
        return new ResponseEntity<>(projectContractDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/employees")
    @Operation(summary = "Gets all employees on a project",
            description = "Retrieves all employees assigned to a specific project")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesOnProject(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        List<EmployeeDTO> employees = projectService.getAllEmployeesOnProject(id, authenticatedUser);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PostMapping("/{projectId}/positions/{positionId}")
    @Operation(summary = "Adds a position to a project",
            description = "Adds an existing position to a project")
    public ResponseEntity<ProjectDTO> addPositionToProject(
            @PathVariable Long projectId,
            @PathVariable Long positionId,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectDTO updatedProject = projectService.addPositionToProject(projectId, positionId, authenticatedUser);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}/positions/{positionId}")
    @Operation(summary = "Removes a position from a project",
            description = "Removes a position from a project")
    public ResponseEntity<ProjectDTO> removePositionFromProject(
            @PathVariable Long projectId,
            @PathVariable Long positionId,
            @AuthenticationPrincipal User authenticatedUser) {
        ProjectDTO updatedProject = projectService.removePositionFromProject(projectId, positionId, authenticatedUser);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @GetMapping("/search/code/{projectCode}")
    @Operation(summary = "Gets projects by code",
            description = "Retrieves projects by their project code")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectCode(
            @PathVariable String projectCode) {
        List<ProjectDTO> projects = projectService.getProjectsByProjectCode(projectCode);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/search/name/{projectName}")
    @Operation(summary = "Gets projects by name",
            description = "Retrieves projects by their project name")
    public ResponseEntity<List<ProjectDTO>> getProjectsByProjectName(
            @PathVariable String projectName) {
        List<ProjectDTO> projects = projectService.getProjectsByProjectName(projectName);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/search/status/{status}")
    @Operation(summary = "Gets projects by status",
            description = "Retrieves projects by their status")
    public ResponseEntity<List<ProjectDTO>> getProjectsByStatus(
            @PathVariable ProjectStatus status) {
        List<ProjectDTO> projects = projectService.getProjectsByProjectStatus(status);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/sort/start-date/asc")
    @Operation(summary = "Gets projects sorted by start date (ascending)",
            description = "Retrieves projects sorted by their start date in ascending order")
    public ResponseEntity<List<ProjectDTO>> getProjectsSortedByStartDateAsc() {
        List<ProjectDTO> projects = projectService.getAllProjectsOrderedByStartDateAsc();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/sort/start-date/desc")
    @Operation(summary = "Gets projects sorted by start date (descending)",
            description = "Retrieves projects sorted by their start date in descending order")
    public ResponseEntity<List<ProjectDTO>> getProjectsSortedByStartDateDesc() {
        List<ProjectDTO> projects = projectService.getAllProjectsOrderedByStartDateDesc();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/sort/end-date/asc")
    @Operation(summary = "Gets projects sorted by end date (ascending)",
            description = "Retrieves projects sorted by their end date in ascending order")
    public ResponseEntity<List<ProjectDTO>> getProjectsSortedByEndDateAsc() {
        List<ProjectDTO> projects = projectService.getAllProjectsOrderedByEndDateAsc();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/sort/end-date/desc")
    @Operation(summary = "Gets projects sorted by end date (descending)",
            description = "Retrieves projects sorted by their end date in descending order")
    public ResponseEntity<List<ProjectDTO>> getProjectsSortedByEndDateDesc() {
        List<ProjectDTO> projects = projectService.getAllProjectsOrderedByEndDateDesc();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}