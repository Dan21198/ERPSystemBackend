package osu.services;

import osu.model.Position;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    Position createPosition(Position position);
    Optional<Position> getPosition(Long id);
    List<Position> getAllPositions();
    Position updatePosition(Long id, Position position);
    void deletePosition(Long id);
}
