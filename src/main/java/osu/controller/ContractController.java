package osu.controller;

import osu.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.services.ContractService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<Contract> createContract(@RequestBody Contract contract) {
        Contract createdContract = contractService.createContract(contract);
        return new ResponseEntity<>(createdContract, HttpStatus.CREATED);
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Contract> getContract(@PathVariable Long orderNumber) {
        Contract contract = contractService.getContract(orderNumber);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Contract>> getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return new ResponseEntity<>(contracts, HttpStatus.OK);
    }

    @PutMapping("/{orderNumber}")
    public ResponseEntity<Contract> updateContract(@PathVariable Long orderNumber, @RequestBody Contract contract) {
        Contract updatedContract = contractService.updateContract(orderNumber, contract);
        return new ResponseEntity<>(updatedContract, HttpStatus.OK);
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteContract(@PathVariable Long orderNumber) {
        contractService.deleteContract(orderNumber);
        return ResponseEntity.noContent().build();
    }
}
