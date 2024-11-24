package osu.position.mapper;

import org.mapstruct.Mapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionDTO toDto(Position position);

    Position toEntity(PositionDTO positionDTO);
}