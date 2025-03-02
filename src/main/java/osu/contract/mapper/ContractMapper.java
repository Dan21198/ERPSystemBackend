package osu.contract.mapper;

import org.mapstruct.*;
import osu.contract.model.Contract;
import osu.contract.model.ContractDTO;
import osu.project.model.Project;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContractMapper {

    @Mapping(source = "projectId", target = "project", qualifiedByName = "projectIdToProject")
    Contract toEntity(ContractDTO contractDTO);

    @Mapping(source = "project.id", target = "projectId")
    ContractDTO toDto(Contract contract);

    @Mapping(source = "projectId", target = "project", qualifiedByName = "projectIdToProject")
    void toEntity(ContractDTO contractDTO, @MappingTarget Contract contract);

    @Named("projectIdToProject")
    default Project projectIdToProject(Long projectId) {
        if (projectId == null) {
            return null;
        }
        Project project = new Project();
        project.setId(projectId);
        return project;
    }
}