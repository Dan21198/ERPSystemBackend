package osu.contract.mapper;

import org.mapstruct.Mapper;
import osu.contract.model.Contract;
import osu.contract.model.ContractDTO;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toEntity(ContractDTO contractDTO);
    ContractDTO toDto(Contract contract);
}