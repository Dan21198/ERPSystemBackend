package osu.position.service;

import osu.position.model.PositionDTO;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(PositionDTO positionDTO);
    Optional<PositionDTO> getPosition(Long id);
    List<PositionDTO> getAllPositions();
    PositionDTO updatePosition(Long id, PositionDTO positionDTO);
    void deletePosition(Long id);
    PositionDTO removeTariffFromPosition(Long id);
    PositionDTO removeEmployeeFromPosition(Long id);
}