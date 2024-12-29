package osu.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.project.model.Project;
import osu.project.model.ProjectDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {
    ProjectDTO toDto(Project project);
    Project toEntity(ProjectDTO projectDTO);
    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(ProjectDTO projectDTO, @MappingTarget Project project);
}
