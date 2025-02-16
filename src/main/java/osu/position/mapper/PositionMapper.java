package osu.position.mapper;

import org.mapstruct.*;
import osu.employee.mapper.EmployeeMapper;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.tariff.mapper.TariffMapper;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {TariffMapper.class, EmployeeMapper.class})
public interface PositionMapper {

    PositionDTO toDto(Position position);

    Position toEntity(PositionDTO positionDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PositionDTO positionDTO,@MappingTarget Position position);
}