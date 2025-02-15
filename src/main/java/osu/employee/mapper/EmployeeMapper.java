package osu.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.employee.model.EmployeeDTO;
import osu.employee.model.Employee;
import osu.position.mapper.PositionMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapper.class})
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO employeeDTO);

    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}