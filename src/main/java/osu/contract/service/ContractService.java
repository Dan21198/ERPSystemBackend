package osu.contract.service;

import osu.contract.model.Contract;

import java.util.List;

public interface ContractService {

    Contract createContract(Contract contract);
    Contract getContract(Long orderNumber);
    List<Contract> getAllContracts();
    Contract updateContract(Long orderNumber, Contract contract);
    void deleteContract(Long orderNumber);
}
