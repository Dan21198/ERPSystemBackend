package osu.controller;

import osu.dto.ProjectDTO;
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
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectRequest) {
        ProjectDTO projectResponse = projectService.createProject(projectRequest);

        return new ResponseEntity<>(projectResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{registrationNumber}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long registrationNumber,
                                                    @RequestBody ProjectDTO projectRequest) {
        ProjectDTO updatedProjectDTO = projectService.updateProject(registrationNumber, projectRequest);

        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }



    @DeleteMapping("/{registrationNumber}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long registrationNumber) {
        projectService.deleteProject(registrationNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long registrationNumber) {
        ProjectDTO projectResponse = projectService.getProject(registrationNumber)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + registrationNumber + " not found"));
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projectResponses = projectService.getAllProjects();
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }
}
