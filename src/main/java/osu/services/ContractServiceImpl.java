package osu.services;

import osu.exception.RecordNotFoundException;
import osu.model.Contract;
import osu.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.BeanUtils;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Contract createContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public Contract getContract(Long orderNumber) {
        return contractRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException("Contract with orderNumber " + orderNumber + " not found"));
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract updateContract(Long orderNumber, Contract contractDetails) {
        Contract existingContract = contractRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException("Contract with orderNumber " + orderNumber + " not found"));

        BeanUtils.copyProperties(contractDetails, existingContract, "orderNumber");

        return contractRepository.save(existingContract);
    }

    @Override
    public void deleteContract(Long orderNumber) {
        Contract existingContract = contractRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException("Contract with orderNumber " + orderNumber + " not found"));
        contractRepository.delete(existingContract);
    }
}