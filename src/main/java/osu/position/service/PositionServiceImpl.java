package osu.position.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.employee.mapper.EmployeeMapper;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.exception.RecordNotFoundException;
import osu.position.mapper.PositionMapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.position.repository.PositionRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public List<EmployeeDTO> getAllEmployeesOnPosition(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RecordNotFoundException("Position not found with id: " + positionId));

        Set<Assignment> assignments = position.getAssignments();

        Set<Employee> employees = assignments.stream()
                .map(Assignment::getEmployee)
                .collect(Collectors.toSet());

        return employees.stream()
                .map(employeeMapper::toDto)
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

        positionRepository.delete(positionToDelete);
    }
}