package osu.mapper;

import org.mapstruct.Mapper;
import osu.dto.EmployeeDTO;
import osu.dto.ProjectDTO;
import osu.model.Employee;
import osu.model.Project;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectDTO toDto(Project project);

    Project toEntity(ProjectDTO projectDTO);

    EmployeeDTO toDto(Employee employee);

    Set<EmployeeDTO> employeesToEmployeeDTOs(Set<Employee> employees);
}