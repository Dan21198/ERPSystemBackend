package osu.position.service;

import osu.employee.model.EmployeeDTO;
import osu.position.model.PositionDTO;
import osu.user.model.User;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(PositionDTO positionDTO, User authenticatedUser);
    Optional<PositionDTO> getPosition(Long id, User authenticatedUser);
    List<PositionDTO> getAllPositions(User authenticatedUser);
    PositionDTO updatePosition(Long id, PositionDTO positionDTO, User authenticatedUser);
    void deletePosition(Long id, User authenticatedUser);
    List<EmployeeDTO> getAllEmployeesOnPosition(Long positionId, User authenticatedUser);
}