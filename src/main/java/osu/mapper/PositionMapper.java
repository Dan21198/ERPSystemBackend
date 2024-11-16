package osu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import osu.dto.PositionDTO;
import osu.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    @Mapping(target = "employees", ignore = true)
    Position toEntity(PositionDTO positionDTO);

    @Mapping(target = "employees", ignore = true)
    PositionDTO toDto(Position position);
}