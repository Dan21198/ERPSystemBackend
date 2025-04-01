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
import osu.user.model.User;

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
    public ContractDTO createContract(ContractDTO contractDTO, User authenticatedUser) {
        Contract contract = contractMapper.toEntity(contractDTO);
        contract.setCreatedBy(authenticatedUser);
        Contract savedContract = contractRepository.save(contract);
        return contractMapper.toDto(savedContract);
    }

    @Override
    public ContractDTO getContract(Long id, User authenticatedUser) {
        Contract contract = contractRepository.findByIdAndCreatedBy(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Contract not found or unauthorized"));
        return contractMapper.toDto(contract);
    }

    @Override
    public List<ContractDTO> getAllContracts(User authenticatedUser) {
        return contractRepository.findByCreatedBy(authenticatedUser).stream()
                .map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContractDTO updateContract(Long id, ContractDTO contractDTO, User authenticatedUser) {
        try {
            Contract existingContract = contractRepository.findByIdAndCreatedBy(id, authenticatedUser)
                    .orElseThrow(() -> new RecordNotFoundException("Contract not found or unauthorized"));

            contractMapper.toEntity(contractDTO, existingContract);

            if (contractDTO.getProjectId() != null) {
                Project project = projectRepository.findById(Long.valueOf(contractDTO.getProjectId()))
                        .orElseThrow(() -> new RecordNotFoundException("Project not found"));
                existingContract.setProject(project);
            }

            Contract updatedContract = contractRepository.save(existingContract);
            return contractMapper.toDto(updatedContract);
        } catch (Exception e) {
            logger.error("Error updating contract", e);
            throw new RuntimeException("Failed to update contract", e);
        }
    }

    @Override
    public void deleteContract(Long id, User authenticatedUser) {
        Contract contract = contractRepository.findByIdAndCreatedBy(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Contract not found or unauthorized"));
        contractRepository.delete(contract);
    }
}