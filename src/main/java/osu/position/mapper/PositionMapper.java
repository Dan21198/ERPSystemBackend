package osu.position.mapper;

import org.mapstruct.*;
import osu.position.model.Position;
import osu.position.model.PositionDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {

    @Mapping(target = "durationInMonths", source = "durationInMonths")
    @Mapping(target = "fte", source = "fte")
    @Mapping(target = "projectId", source = "project.id")
    PositionDTO toDto(Position position);

    @Mapping(target = "durationInMonths", ignore = true)
    @Mapping(target = "fte", ignore = true)
    @Mapping(target = "project.id", source = "projectId")
    Position toEntity(PositionDTO positionDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "durationInMonths", ignore = true)
    @Mapping(target = "fte", ignore = true)
    @Mapping(target = "project.id", source = "projectId")
    void updateEntityFromDto(PositionDTO positionDTO, @MappingTarget Position position);
}