package osu.assignment.service;

import osu.assignment.model.AssignmentDTO;
import osu.user.model.User;

import java.util.List;

public interface AssignmentService {
    AssignmentDTO createAssignment(AssignmentDTO assignmentDTO, User authenticatedUser);
    AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO, User authenticatedUser);
    void deleteAssignment(Long id, User authenticatedUser);
    AssignmentDTO getAssignmentById(Long id, User authenticatedUser);
    List<AssignmentDTO> getAllAssignments(User authenticatedUser);
    AssignmentDTO assignTariffAndEmployeeToPosition(AssignmentDTO assignmentDTO, User authenticatedUser);
    AssignmentDTO deactivateAssignment(Long id, User authenticatedUser);
}