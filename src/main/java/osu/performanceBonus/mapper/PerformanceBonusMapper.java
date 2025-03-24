package osu.performanceBonus.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PerformanceBonusMapper {
    PerformanceBonusDTO toDto(PerformanceBonus bonus);

    @Mapping(target = "employee", ignore = true)
    PerformanceBonus toEntity(PerformanceBonusDTO bonusDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateBonusFromDto(PerformanceBonusDTO bonusDTO, @MappingTarget PerformanceBonus bonus);
}
