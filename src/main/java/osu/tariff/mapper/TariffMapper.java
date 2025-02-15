package osu.tariff.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.tariff.model.Tariff;
import osu.tariff.model.TariffDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TariffMapper {
    TariffDTO toDto(Tariff tariff);

    Tariff toEntity(TariffDTO tariffDTO);

    @Mapping(target = "id", ignore = true)
    void updateTariffFromDto(TariffDTO tariffDTO, @MappingTarget Tariff existingTariff);
}
