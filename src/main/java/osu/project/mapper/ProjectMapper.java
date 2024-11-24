package osu.project.mapper;

import org.mapstruct.Mapper;
import osu.project.model.Project;
import osu.project.model.ProjectDTO;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO toDto(Project project);
    Project toEntity(ProjectDTO projectDTO);
}
