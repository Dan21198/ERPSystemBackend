package osu.contract.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.contract.service.ContractService;
import osu.contract.model.Contract;

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
    @Operation(
            summary = "Create a new contract",
            description = "Creates a new contract and returns the created contract details"
    )
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a contract by ID",
            description = "Retrieves the details of a contract by its ID"
    )
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        Contract contract = contractService.getContract(id);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @GetMapping
    @Operation(
            summary = "Get all contracts",
            description = "Retrieves a list of all contracts"
    )
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a contract",
            description = "Updates the details of an existing contract by its ID"
    )
    public ResponseEntity<Contract> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(id, contract);
        return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a contract",
            description = "Deletes a contract by its ID"
    )
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
