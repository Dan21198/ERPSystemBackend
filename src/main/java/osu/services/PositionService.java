package osu.services;

import osu.dto.PositionDTO;
import osu.model.Position;
import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(PositionDTO positionDTO);
    Optional<PositionDTO> getPosition(Long id);
    List<PositionDTO> getAllPositions();
    PositionDTO updatePosition(Long id, PositionDTO positionDTO);
    void deletePosition(Long id);
}