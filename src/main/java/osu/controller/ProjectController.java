package osu.controller;

import osu.dto.ProjectDTO;
import osu.exception.RecordNotFoundException;
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

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id,
                                                    @RequestBody ProjectDTO projectRequest) {
        ProjectDTO updatedProjectDTO = projectService.updateProject(id, projectRequest);

        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        ProjectDTO projectResponse = projectService.getProject(id)
                .orElseThrow(() -> new RecordNotFoundException("Project with registration number "
                        + id + " not found"));
        return new ResponseEntity<>(projectResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projectResponses = projectService.getAllProjects();
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }
}
