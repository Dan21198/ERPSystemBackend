package osu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import osu.dto.PositionDTO;
import osu.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionDTO toDto(Position position);

    Position toEntity(PositionDTO positionDTO);
}