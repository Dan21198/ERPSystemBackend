package osu.employee.mapper;

import org.mapstruct.*;
import osu.assignment.model.Assignment;
import osu.assignment.model.AssignmentDTO;
import osu.employee.model.Employee;
import osu.employee.model.EmployeeDTO;
import osu.employee.service.SalaryCalculator;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = SalaryCalculator.class)
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
    @Mapping(target = "grossSalary", ignore = true) // Salary will be calculated by service layer
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
        return employee.getAssignments().stream()
                .filter(Assignment::isActive)
                .map(assignment -> assignment.getTariff().getWageClass())
                .findFirst()
                .orElse(0);
    }

    @Named("mapTariffAmount")
    default double mapTariffAmount(Employee employee) {
        return employee.getAssignments().stream()
                .filter(Assignment::isActive)
                .mapToDouble(assignment -> assignment.getTariff().getWageTariff()
                        * (assignment.getAllocatedTimePercentage() / 100.0))
                .sum();
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