package osu.employee.mapper;

import org.mapstruct.*;
import osu.assignment.model.Assignment;
import osu.assignment.model.AssignmentDTO;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "mapAssignmentsToDTOs")
    @Mapping(target = "wageClass", source = "employee", qualifiedByName = "mapWageClass")
    @Mapping(target = "tariffAmount", source = "employee", qualifiedByName = "mapTariffAmount")
    @Mapping(target = "grossSalary", source = "grossSalary")
    @Mapping(target = "contractAboutToExpire", source = "contractAboutToExpire")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "mapDTOsToAssignments")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "mapDTOsToAssignments")
    @Mapping(target = "contractAboutToExpire", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

    @Named("mapAssignmentsToDTOs")
    default Set<AssignmentDTO> mapAssignmentsToDTOs(Set<Assignment> assignments) {
        if (assignments == null) {
            return Set.of();
        }
        return assignments.stream()
                .map(assignment -> {
                    AssignmentDTO dto = toAssignmentDto(assignment);
                    if (dto.getPerformanceBonuses() != null && assignment.getId() != null) {
                        dto.getPerformanceBonuses().forEach(bonus ->
                                bonus.setAssignmentId(assignment.getId()));
                    }
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    @Named("mapDTOsToAssignments")
    default Set<Assignment> mapDTOsToAssignments(Set<AssignmentDTO> assignmentDTOs) {
        if (assignmentDTOs == null) {
            return Set.of();
        }
        return assignmentDTOs.stream()
                .map(this::toAssignmentEntity)
                .collect(Collectors.toSet());
    }

    @Named("mapWageClass")
    default int mapWageClass(Employee employee) {
        return employee.getCurrentWageClass();
    }

    @Named("mapTariffAmount")
    default double mapTariffAmount(Employee employee) {
        return employee.getCurrentTariffAmount();
    }

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "tariff.id", target = "tariffId")
    AssignmentDTO toAssignmentDto(Assignment assignment);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "positionId", target = "position.id")
    @Mapping(source = "tariffId", target = "tariff.id")
    Assignment toAssignmentEntity(AssignmentDTO assignmentDTO);
}