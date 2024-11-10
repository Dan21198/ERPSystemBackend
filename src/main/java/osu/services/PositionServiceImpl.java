package osu.services;

import osu.exception.RecordNotFoundException;
import osu.model.Position;
import osu.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public Optional<Position> getPosition(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public Position updatePosition(Long id, Position position) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        existingPosition.setName(position.getName());

        return positionRepository.save(existingPosition);
    }

    @Override
    public void deletePosition(Long id) {
        Position positionToDelete = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        positionRepository.delete(positionToDelete);
    }
}