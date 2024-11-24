package osu.employee.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import osu.employee.model.EmployeeDTO;
import osu.employee.model.Employee;
import osu.mapper.CustomMappings;

@Mapper(componentModel = "spring", uses = CustomMappings.class)
public interface EmployeeMapper {
    @Mapping(target = "title", source = "title", qualifiedByName = "mapTitle")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "title", source = "title", qualifiedByName = "mapTitle")
    EmployeeDTO toDto(Employee employee);
}