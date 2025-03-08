package osu.contract.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import osu.contract.mapper.ContractMapper;
import osu.contract.model.Contract;
import osu.contract.model.ContractDTO;
import osu.contract.repository.ContractRepository;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.project.model.Project;
import osu.project.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, ContractMapper contractMapper,
                               ProjectRepository projectRepository) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    @Transactional
    public ContractDTO createContract(ContractDTO contractDTO) {
        Contract contract = contractMapper.toEntity(contractDTO);
        Contract savedContract = contractRepository.save(contract);
        return contractMapper.toDto(savedContract);
    }

    @Override
    public ContractDTO getContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Contract with ID " + id + " not found"));
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
    @Transactional
    public ContractDTO updateContract(Long id, ContractDTO contractDTO) {
        try {
            Contract existingContract = contractRepository.findById(id)
                    .orElseThrow(() -> new RecordNotFoundException("Contract with ID " + id + " not found"));

            contractMapper.toEntity(contractDTO, existingContract);

            String originalOrderName = existingContract.getOrderName();
            if (existingContract.getOrderName() == null || existingContract.getOrderName().isBlank()) {
                existingContract.setOrderName(originalOrderName);
            }

            if (contractDTO.getProjectId() != null) {
                Project project = projectRepository.findById(Long.valueOf(contractDTO.getProjectId()))
                        .orElseThrow(() -> new RecordNotFoundException("Project with ID " + contractDTO.getProjectId()
                                + " not found"));
                existingContract.setProject(project);
            }

            Contract updatedContract = contractRepository.save(existingContract);

            return contractMapper.toDto(updatedContract);
        } catch (Exception e) {
            logger.error("Error updating contract with ID: {}", id, e);
            throw new RuntimeException("Failed to update contract: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteContract(Long id) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Contract with ID " + id + " not found"));
        contractRepository.delete(existingContract);
    }
}