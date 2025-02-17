package osu.contract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import osu.contract.model.Contract;
import osu.contract.model.ContractDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContractMapper {
    Contract toEntity(ContractDTO contractDTO);
    ContractDTO toDto(Contract contract);

    void toEntity(ContractDTO contractDTO, @MappingTarget Contract contract);
}