package osu.performanceBonus.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.performanceBonus.service.PerformanceBonusService;

@RestController
@RequestMapping("/api/v1/bonuses/{employeeId}/bonuses")
public class PerformanceBonusController {

    private final PerformanceBonusService performanceBonusService;

    @Autowired
    public PerformanceBonusController(PerformanceBonusService performanceBonusService) {
        this.performanceBonusService = performanceBonusService;
    }

    @PostMapping
    @Operation(summary = "Add a performance bonus to an employee")
    public ResponseEntity<PerformanceBonusDTO> addBonus(
            @PathVariable Long employeeId,
            @RequestBody PerformanceBonusDTO bonusDTO) {
        PerformanceBonusDTO createdBonus = performanceBonusService.addBonus(employeeId, bonusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBonus);
    }

    @DeleteMapping("/{bonusId}")
    @Operation(summary = "Remove a performance bonus from an employee")
    public ResponseEntity<Void> removeBonus(
            @PathVariable Long employeeId,
            @PathVariable Long bonusId) {
        performanceBonusService.removeBonus(employeeId, bonusId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bonusId}")
    @Operation(summary = "Update a performance bonus")
    public ResponseEntity<PerformanceBonusDTO> updateBonus(
            @PathVariable Long employeeId,
            @PathVariable Long bonusId,
            @RequestBody PerformanceBonusDTO bonusDTO) {
        PerformanceBonusDTO updatedBonus = performanceBonusService.updateBonus(employeeId, bonusId, bonusDTO);
        return ResponseEntity.ok(updatedBonus);
    }
}
