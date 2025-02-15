package osu.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.employee.mapper.EmployeeMapper;
import osu.project.model.Project;
import osu.project.model.ProjectDTO;
import osu.position.mapper.PositionMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapper.class, EmployeeMapper.class})
public interface ProjectMapper {
    ProjectDTO toDto(Project project);
    Project toEntity(ProjectDTO projectDTO);
    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(ProjectDTO projectDTO, @MappingTarget Project project);
}
