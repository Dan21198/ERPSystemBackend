package osu.contract.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import osu.contract.model.ContractDTO;
import osu.contract.service.ContractService;
import osu.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    @Operation(summary = "Create a new contract", description = "Creates a new contract")
    public ResponseEntity<ContractDTO> createContract(
            @RequestBody ContractDTO contractDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        ContractDTO createdContract = contractService.createContract(contractDTO, authenticatedUser);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contract by ID", description = "Retrieves contract details")
    public ResponseEntity<ContractDTO> getContract(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        ContractDTO contract = contractService.getContract(id, authenticatedUser);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all contracts", description = "Retrieves all contracts")
    public ResponseEntity<List<ContractDTO>> getAllContracts(
            @AuthenticationPrincipal User authenticatedUser) {
        List<ContractDTO> contracts = contractService.getAllContracts(authenticatedUser);
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contract", description = "Updates contract details")
    public ResponseEntity<ContractDTO> updateContract(
            @PathVariable Long id,
            @RequestBody ContractDTO contractDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        ContractDTO updatedContract = contractService.updateContract(id, contractDTO, authenticatedUser);
        return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contract", description = "Deletes a contract")
    public ResponseEntity<Void> deleteContract(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        contractService.deleteContract(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}