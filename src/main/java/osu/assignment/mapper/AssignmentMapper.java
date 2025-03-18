package osu.assignment.mapper;

import org.mapstruct.*;
import osu.assignment.model.Assignment;
import osu.assignment.model.AssignmentDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapperHelper.class, TariffMapperHelper.class})
public interface AssignmentMapper {

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "positionId", target = "position", qualifiedByName = "mapPosition")
    @Mapping(source = "tariffId", target = "tariff", qualifiedByName = "mapTariff")
    Assignment toEntity(AssignmentDTO dto);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "tariff.id", target = "tariffId")
    AssignmentDTO toDTO(Assignment assignment);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "positionId", target = "position", qualifiedByName = "mapPosition")
    @Mapping(source = "tariffId", target = "tariff", qualifiedByName = "mapTariff")
    void updateEntityFromDTO(AssignmentDTO dto, @MappingTarget Assignment entity);
}