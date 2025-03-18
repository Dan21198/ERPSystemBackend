package osu.position.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.repository.AssignmentRepository;
import osu.exception.RecordNotFoundException;
import osu.position.mapper.PositionMapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.position.repository.PositionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository,
                               PositionMapper positionMapper,
                               AssignmentRepository assignmentRepository) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public PositionDTO createPosition(PositionDTO positionDTO) {
        Position position = positionMapper.toEntity(positionDTO);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDto(savedPosition);
    }

    @Override
    public Optional<PositionDTO> getPosition(Long id) {
        Optional<Position> position = positionRepository.findById(id);
        return position.map(positionMapper::toDto);
    }

    @Override
    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(Long id, PositionDTO positionDTO) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        positionMapper.updateEntityFromDto(positionDTO, existingPosition);

        Position updatedPosition = positionRepository.save(existingPosition);
        return positionMapper.toDto(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(Long id) {
        Position positionToDelete = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        assignmentRepository.deleteByPosition(positionToDelete);

        positionRepository.delete(positionToDelete);
    }
}