package osu.mapper;

import org.mapstruct.Mapper;
import osu.dto.PositionDTO;
import osu.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    Position toEntity(PositionDTO positionDTO);
    PositionDTO toDto(Position position);
}