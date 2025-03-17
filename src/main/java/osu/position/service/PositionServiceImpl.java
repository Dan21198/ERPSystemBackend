package osu.position.service;

import jakarta.transaction.Transactional;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.position.mapper.PositionMapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.position.repository.PositionRepository;
import osu.tariff.model.Tariff;
import osu.tariff.repository.TariffRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final EmployeeRepository employeeRepository;
    private final TariffRepository tariffRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper, EmployeeRepository employeeRepository, TariffRepository tariffRepository) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.employeeRepository = employeeRepository;
        this.tariffRepository = tariffRepository;
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

        // Update tariff if provided
        if (positionDTO.getTariff() != null && positionDTO.getTariff().getId() != null) {
            Tariff tariff = tariffRepository.findById(positionDTO.getTariff().getId())
                    .orElseThrow(() -> new RecordNotFoundException("Tariff with ID " +
                            positionDTO.getTariff().getId() + " not found"));
            existingPosition.setTariff(tariff);
        }

        if (positionDTO.getEmployee() != null && positionDTO.getEmployee().getId() != null) {
            Employee employee = employeeRepository.findById(positionDTO.getEmployee().getId())
                    .orElseThrow(() -> new RecordNotFoundException("Employee with ID " +
                            positionDTO.getEmployee().getId() + " not found"));

            if (existingPosition.getEmployee() != null && existingPosition.getEmployee().getId() != null &&
                    !existingPosition.getEmployee().getId().equals(employee.getId())) {
                existingPosition.getEmployee().getPositions().remove(existingPosition);
            }

            existingPosition.setEmployee(employee);
            employee.getPositions().add(existingPosition);

            if (existingPosition.getTariff() != null) {
                employee.calculateGrossSalary();
                employeeRepository.save(employee);
            }
        }

        Position updatedPosition = positionRepository.save(existingPosition);
        return positionMapper.toDto(updatedPosition);
    }

    @Override
    public void deletePosition(Long id) {
        Position positionToDelete = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        positionRepository.delete(positionToDelete);
    }

    @Override
    @Transactional
    public PositionDTO removeTariffFromPosition(Long id) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        existingPosition.setTariff(null);

        Position updatedPosition = positionRepository.save(existingPosition);

        return positionMapper.toDto(updatedPosition);
    }

    @Override
    @Transactional
    public PositionDTO removeEmployeeFromPosition(Long id) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Position with ID " + id + " not found"));

        if (existingPosition.getEmployee() != null) {
            Employee employee = existingPosition.getEmployee();
            employee.getPositions().remove(existingPosition);
            existingPosition.setEmployee(null);
            employeeRepository.save(employee);
        }

        Position updatedPosition = positionRepository.save(existingPosition);

        return positionMapper.toDto(updatedPosition);
    }
}

