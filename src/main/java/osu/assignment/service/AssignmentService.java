package osu.assignment.service;

import osu.assignment.model.AssignmentDTO;
import java.util.List;

public interface AssignmentService {
    AssignmentDTO createAssignment(AssignmentDTO assignmentDTO);
    AssignmentDTO updateAssignment(Long id, AssignmentDTO assignmentDTO);
    void deleteAssignment(Long id);
    AssignmentDTO getAssignmentById(Long id);
    List<AssignmentDTO> getAllAssignments();
    AssignmentDTO assignTariffAndEmployeeToPosition(AssignmentDTO assignmentDTO);
    AssignmentDTO deactivateAssignment(Long id);
}