package osu.position.mapper;

import org.mapstruct.*;
import osu.position.model.Position;
import osu.position.model.PositionDTO;
import osu.position.service.PositionCalculationService;
import osu.config.SpringContext;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PositionMapper {

    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "totalAmountSpent", source = "totalAmountSpent")
    PositionDTO toDto(Position position, @Context PositionCalculationService calculationService);

    default PositionDTO toDto(Position position) {
        return toDto(position, SpringContext.getBean(PositionCalculationService.class));
    }

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "totalAmountSpent", ignore = true)
    Position toEntity(PositionDTO positionDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "totalAmountSpent", ignore = true)
    void updateEntityFromDto(PositionDTO positionDTO, @MappingTarget Position position);

    @AfterMapping
    default void mapComputedFields(Position position, @MappingTarget PositionDTO dto,
                                   @Context PositionCalculationService calculationService) {
        if (position != null) {
            if (position.getStartDate() != null && position.getEndDate() != null) {
                dto.setDurationInMonths(calculationService.calculateDurationInMonths(position));
                dto.setFte(calculationService.calculateFte(position));
            }

            Double amount = position.getTotalAmountSpent();
            dto.setTotalAmountSpent(amount != null ? amount : 0.0);
        }
    }
}