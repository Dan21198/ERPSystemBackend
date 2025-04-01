package osu.assignment.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import osu.assignment.model.AssignmentDTO;
import osu.assignment.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentDTO> createAssignment(
            @RequestBody AssignmentDTO assignmentDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        AssignmentDTO createdAssignment = assignmentService.createAssignment(assignmentDTO, authenticatedUser);
        return ResponseEntity.ok(createdAssignment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(
            @PathVariable Long id,
            @RequestBody AssignmentDTO assignmentDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        AssignmentDTO updatedAssignment = assignmentService.updateAssignment(id, assignmentDTO, authenticatedUser);
        return ResponseEntity.ok(updatedAssignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        assignmentService.deleteAssignment(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        AssignmentDTO assignment = assignmentService.getAssignmentById(id, authenticatedUser);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments(
            @AuthenticationPrincipal User authenticatedUser) {
        List<AssignmentDTO> assignments = assignmentService.getAllAssignments(authenticatedUser);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/assign-tariff-and-employee")
    public ResponseEntity<AssignmentDTO> assignTariffAndEmployeeToPosition(
            @RequestBody AssignmentDTO assignmentDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        AssignmentDTO createdAssignment = assignmentService.assignTariffAndEmployeeToPosition(assignmentDTO, authenticatedUser);
        return ResponseEntity.ok(createdAssignment);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<AssignmentDTO> deactivateAssignment(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        AssignmentDTO deactivatedAssignment = assignmentService.deactivateAssignment(id, authenticatedUser);
        return ResponseEntity.ok(deactivatedAssignment);
    }
}