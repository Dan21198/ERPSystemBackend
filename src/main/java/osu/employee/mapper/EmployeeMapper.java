package osu.employee.mapper;

import org.mapstruct.*;
import osu.employee.model.EmployeeDTO;
import osu.employee.model.Employee;
import osu.position.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    @Mapping(target = "positionIds", source = "positions", qualifiedByName = "mapPositionsToPositionIds")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "positions", source = "positionIds", qualifiedByName = "mapPositionIdsToPositions")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "positions", source = "positionIds", qualifiedByName = "mapPositionIdsToPositions")
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

    @Named("mapPositionsToPositionIds")
    default Set<Long> mapPositionsToPositionIds(Set<Position> positions) {
        if (positions == null) {
            return null;
        }
        return positions.stream()
                .map(Position::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapPositionIdsToPositions")
    default Set<Position> mapPositionIdsToPositions(Set<Long> positionIds) {
        if (positionIds == null) {
            return null;
        }
        return positionIds.stream()
                .map(id -> {
                    Position position = new Position();
                    position.setId(id);
                    return position;
                })
                .collect(Collectors.toSet());
    }
}