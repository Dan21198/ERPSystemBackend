package osu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import osu.dto.EmployeeDTO;
import osu.model.Employee;

@Mapper(componentModel = "spring", uses = CustomMappings.class)
public interface EmployeeMapper {
    @Mapping(target = "title", source = "title", qualifiedByName = "mapTitle")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "title", source = "title", qualifiedByName = "mapTitle")
    EmployeeDTO toDto(Employee employee);
}