package osu.project.mapper;

import org.mapstruct.*;
import osu.contract.mapper.ContractMapper;
import osu.employee.mapper.EmployeeMapper;
import osu.project.model.Project;
import osu.project.model.ProjectContractDTO;
import osu.project.model.ProjectDTO;
import osu.position.mapper.PositionMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PositionMapper.class, EmployeeMapper.class, ContractMapper.class})
public interface ProjectMapper {

    @Mapping(target = "totalAmountSpent", expression = "java(calculateTotalAmountSpent(project))")
    ProjectDTO toDto(Project project);

    Project toEntity(ProjectDTO projectDTO);

    @Mapping(target = "id", ignore = true)
    void updateProjectFromDto(ProjectDTO projectDTO, @MappingTarget Project project);

    @Mapping(source = "id", target = "projectId")
    @Mapping(source = "contracts", target = "contracts")
    @Mapping(target = "totalAmountSpent", expression = "java(calculateTotalAmountSpent(project))")
    @Mapping(source = "totalAmountAllocated", target = "totalAmountAllocated")
    ProjectContractDTO toProjectContractDto(Project project);

    default Double calculateTotalAmountSpent(Project project) {
        if (project == null || project.getPositions() == null || project.getPositions().isEmpty()) {
            return 0.0;
        }

        return project.getPositions().stream()
                .filter(java.util.Objects::nonNull)
                .mapToDouble(position -> {
                    Double amount = position.getTotalAmountSpent();
                    return amount != null ? amount : 0.0;
                })
                .sum();
    }
}