package osu.mapper;

import org.mapstruct.Mapper;
import osu.dto.ContractDTO;
import osu.model.Contract;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toEntity(ContractDTO contractDTO);
    ContractDTO toDto(Contract contract);
}