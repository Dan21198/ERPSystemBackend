package osu.position.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.employee.mapper.EmployeeMapper;
import osu.employee.model.EmployeeDTO;
import osu.exception.RecordNotFoundException;
import osu.position.mapper.PositionMapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.position.repository.PositionRepository;
import osu.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository,
                               PositionMapper positionMapper, EmployeeMapper employeeMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public PositionDTO createPosition(PositionDTO positionDTO, User authenticatedUser) {
        Position position = positionMapper.toEntity(positionDTO);
        position.setCreatedBy(authenticatedUser);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDto(savedPosition);
    }

    @Override
    public Optional<PositionDTO> getPosition(Long id, User authenticatedUser) {
        return positionRepository.findByIdAndCreatedBy(id, authenticatedUser)
                .map(positionMapper::toDto);
    }

    @Override
    public List<PositionDTO> getAllPositions(User authenticatedUser) {
        return positionRepository.findByCreatedBy(authenticatedUser).stream()
                .map(positionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(Long id, PositionDTO positionDTO, User authenticatedUser) {
        Position existingPosition = positionRepository.findByIdAndCreatedBy(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Position not found or unauthorized"));

        positionMapper.updateEntityFromDto(positionDTO, existingPosition);
        Position updatedPosition = positionRepository.save(existingPosition);
        return positionMapper.toDto(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(Long id, User authenticatedUser) {
        Position position = positionRepository.findByIdAndCreatedBy(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Position not found or unauthorized"));
        positionRepository.delete(position);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesOnPosition(Long positionId, User authenticatedUser) {
        Position position = positionRepository.findByIdAndCreatedBy(positionId, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Position not found or unauthorized"));

        return position.getAssignments().stream()
                .map(Assignment::getEmployee)
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
}