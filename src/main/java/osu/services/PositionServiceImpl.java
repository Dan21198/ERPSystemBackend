package osu.services;

import osu.dto.PositionDTO;
import osu.exception.RecordNotFoundException;
import osu.model.Position;
import osu.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public PositionDTO createPosition(Position position) {
        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    @Override
    public Optional<PositionDTO> getPosition(Long id) {
        Optional<Position> position = positionRepository.findById(id);
        return position.map(this::convertToDTO);
    }

    @Override
    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PositionDTO updatePosition(Long id, Position position) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        existingPosition.setName(position.getName());
        Position updatedPosition = positionRepository.save(existingPosition);
        return convertToDTO(updatedPosition);
    }

    @Override
    public void deletePosition(Long id) {
        Position positionToDelete = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        positionRepository.delete(positionToDelete);
    }

    private PositionDTO convertToDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        return positionDTO;
    }
}
