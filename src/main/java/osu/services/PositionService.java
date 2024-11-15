package osu.services;

import osu.dto.PositionDTO;
import osu.model.Position;
import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(Position position);
    Optional<PositionDTO> getPosition(Long id);
    List<PositionDTO> getAllPositions();
    PositionDTO updatePosition(Long id, Position position);
    void deletePosition(Long id);
}
