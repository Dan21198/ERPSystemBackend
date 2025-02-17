package osu.contract.service;

import osu.contract.model.ContractDTO;

import java.util.List;

public interface ContractService {

    ContractDTO createContract(ContractDTO contractDTO);
    ContractDTO getContract(Long orderNumber);
    List<ContractDTO> getAllContracts();
    ContractDTO updateContract(Long orderNumber, ContractDTO contractDTO);
    void deleteContract(Long orderNumber);
}