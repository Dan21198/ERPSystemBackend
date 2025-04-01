package osu.performanceBonus.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.performanceBonus.service.PerformanceBonusService;
import osu.user.model.User;

@RestController
@RequestMapping("/api/v1/assignments/{assignmentId}/bonuses")
public class PerformanceBonusController {

    private final PerformanceBonusService performanceBonusService;

    @Autowired
    public PerformanceBonusController(PerformanceBonusService performanceBonusService) {
        this.performanceBonusService = performanceBonusService;
    }


    @PostMapping
    @Operation(summary = "Add a performance bonus to an assignment")
    public ResponseEntity<PerformanceBonusDTO> addBonus(
            @PathVariable Long assignmentId,
            @RequestBody PerformanceBonusDTO bonusDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        PerformanceBonusDTO createdBonus = performanceBonusService.addBonus(assignmentId, bonusDTO, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBonus);
    }

    @DeleteMapping("/{bonusId}")
    @Operation(summary = "Remove a performance bonus from an assignment")
    public ResponseEntity<Void> removeBonus(
            @PathVariable Long assignmentId,
            @PathVariable Long bonusId,
            @AuthenticationPrincipal User authenticatedUser) {
        performanceBonusService.removeBonus(assignmentId, bonusId, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bonusId}")
    @Operation(summary = "Update a performance bonus")
    public ResponseEntity<PerformanceBonusDTO> updateBonus(
            @PathVariable Long assignmentId,
            @PathVariable Long bonusId,
            @RequestBody PerformanceBonusDTO bonusDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        PerformanceBonusDTO updatedBonus = performanceBonusService.updateBonus(assignmentId, bonusId, bonusDTO, authenticatedUser);
        return ResponseEntity.ok(updatedBonus);
    }
}