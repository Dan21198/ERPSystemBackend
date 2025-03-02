package osu.contract.service;

import osu.contract.mapper.ContractMapper;
import osu.contract.model.Contract;
import osu.contract.model.ContractDTO;
import osu.contract.repository.ContractRepository;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    @Override
    public ContractDTO createContract(ContractDTO contractDTO) {
        Contract contract = contractMapper.toEntity(contractDTO);
        Contract savedContract = contractRepository.save(contract);
        return contractMapper.toDto(savedContract);
    }

    @Override
    public ContractDTO getContract(Long orderNumber) {
        Contract contract = contractRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException("Contract with orderNumber " + orderNumber + " not found"));
        return contractMapper.toDto(contract);
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        List<Contract> contracts = contractRepository.findAll();
        return contracts.stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDTO updateContract(Long id, ContractDTO contractDTO) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Contract with ID " + id + " not found"));

        contractMapper.toEntity(contractDTO, existingContract);

        Contract updatedContract = contractRepository.save(existingContract);
        return contractMapper.toDto(updatedContract);
    }

    @Override
    public void deleteContract(Long orderNumber) {
        Contract existingContract = contractRepository.findById(orderNumber)
                .orElseThrow(() -> new RecordNotFoundException("Contract with orderNumber " + orderNumber + " not found"));
        contractRepository.delete(existingContract);
    }
}