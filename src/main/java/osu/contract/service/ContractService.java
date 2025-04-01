package osu.contract.service;

import osu.contract.model.ContractDTO;
import osu.user.model.User;

import java.util.List;

public interface ContractService {
    ContractDTO createContract(ContractDTO contractDTO, User authenticatedUser);
    ContractDTO getContract(Long orderNumber, User authenticatedUser);
    List<ContractDTO> getAllContracts(User authenticatedUser);
    ContractDTO updateContract(Long orderNumber, ContractDTO contractDTO, User authenticatedUser);
    void deleteContract(Long orderNumber, User authenticatedUser);
}