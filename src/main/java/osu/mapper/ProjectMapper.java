package osu.mapper;

import org.mapstruct.Mapper;
import osu.dto.ProjectDTO;
import osu.model.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectDTO projectDTO);
    ProjectDTO toDto(Project project);
}