package osu.controller;

import osu.dto.ProjectRequest;
import osu.exception.RecordNotFoundException;
import osu.model.Project;
import osu.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequest projectRequest) {
        Project project = projectService.createProject(projectRequest);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping("/{registrationNumber}")
    public ResponseEntity<Project> updateProject(@PathVariable Long registrationNumber,
                                                 @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(registrationNumber, projectDetails);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{registrationNumber}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long registrationNumber) {
        projectService.deleteProject(registrationNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<Project> getProject(@PathVariable Long registrationNumber) {
        Project project = projectService.getProject(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
