package osu.performanceBonus.mapper;

import org.mapstruct.*;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PerformanceBonusMapper {

    @Mapping(target = "assignmentId", source = "assignment.id")
    PerformanceBonusDTO toDto(PerformanceBonus bonus);

    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "assignment.id", source = "assignmentId")
    PerformanceBonus toEntity(PerformanceBonusDTO bonusDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "assignment.id", source = "assignmentId")
    void updateBonusFromDto(PerformanceBonusDTO bonusDTO, @MappingTarget PerformanceBonus bonus);

}