package osu.position.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.position.model.Position;
import osu.position.model.PositionDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {

    PositionDTO toDto(Position position);

    Position toEntity(PositionDTO positionDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateEntityFromDto(PositionDTO positionDTO,@MappingTarget Position existingPosition);
}