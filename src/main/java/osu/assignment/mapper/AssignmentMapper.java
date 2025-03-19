package osu.assignment.mapper;

import org.mapstruct.*;
import osu.assignment.model.Assignment;
import osu.assignment.model.AssignmentDTO;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;
import osu.tariff.model.Tariff;
import osu.tariff.repository.TariffRepository;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapperHelper.class, TariffMapperHelper.class})
public interface AssignmentMapper {

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "positionId", target = "position", qualifiedByName = "mapPosition")
    @Mapping(source = "tariffId", target = "tariff", qualifiedByName = "mapTariff")
    Assignment toEntity(AssignmentDTO dto);

    default Assignment toEntity(AssignmentDTO dto, EmployeeRepository employeeRepository,
                                PositionRepository positionRepository, TariffRepository tariffRepository) {
        Assignment assignment = toEntity(dto);
        afterToEntity(dto, assignment, employeeRepository, positionRepository, tariffRepository);
        return assignment;
    }

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "tariff.id", target = "tariffId")
    AssignmentDTO toDTO(Assignment assignment);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "positionId", target = "position", qualifiedByName = "mapPosition")
    @Mapping(source = "tariffId", target = "tariff", qualifiedByName = "mapTariff")
    void updateEntityFromDTO(AssignmentDTO dto, @MappingTarget Assignment entity);

    @AfterMapping
    default void afterToEntity(AssignmentDTO dto, @MappingTarget Assignment entity,
                               @Context EmployeeRepository employeeRepository,
                               @Context PositionRepository positionRepository,
                               @Context TariffRepository tariffRepository) {
        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + dto.getEmployeeId()));
            entity.setEmployee(employee);
        }

        if (dto.getPositionId() != null) {
            Position position = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found with id: " + dto.getPositionId()));
            entity.setPosition(position);
        }

        if (dto.getTariffId() != null) {
            Tariff tariff = tariffRepository.findById(dto.getTariffId())
                    .orElseThrow(() -> new RuntimeException("Tariff not found with id: " + dto.getTariffId()));
            entity.setTariff(tariff);
        }
    }
}