package osu.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.contract.mapper.ContractMapper;
import osu.employee.mapper.EmployeeMapper;
import osu.project.model.Project;
import osu.project.model.ProjectContractDTO;
import osu.project.model.ProjectDTO;
import osu.position.mapper.PositionMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapper.class, EmployeeMapper.class, ContractMapper.class})
public interface ProjectMapper {
    ProjectDTO toDto(Project project);

    Project toEntity(ProjectDTO projectDTO);

    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(ProjectDTO projectDTO, @MappingTarget Project project);

    @Mapping(source = "id", target = "projectId")
    @Mapping(source = "contracts", target = "contracts")
    ProjectContractDTO toProjectContractDto(Project project);
}
