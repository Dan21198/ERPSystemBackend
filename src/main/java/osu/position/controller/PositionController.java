package osu.position.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import osu.employee.model.EmployeeDTO;
import osu.exception.RecordNotFoundException;
import osu.position.service.PositionService;
import osu.position.model.PositionDTO;
import osu.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    @Operation(summary = "Creates a position", description = "Creates a new position")
    public ResponseEntity<PositionDTO> createPosition(
            @Valid @RequestBody PositionDTO positionDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        PositionDTO createdPosition = positionService.createPosition(positionDTO, authenticatedUser);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets a position", description = "Retrieves a position by its ID")
    public ResponseEntity<PositionDTO> getPosition(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        PositionDTO position = positionService.getPosition(id, authenticatedUser)
                .orElseThrow(() -> new RecordNotFoundException("Position not found"));
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Gets all positions", description = "Retrieves all positions")
    public ResponseEntity<List<PositionDTO>> getAllPositions(
            @AuthenticationPrincipal User authenticatedUser) {
        List<PositionDTO> positions = positionService.getAllPositions(authenticatedUser);
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a position", description = "Updates an existing position by its ID")
    public ResponseEntity<PositionDTO> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody PositionDTO positionDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        PositionDTO updatedPosition = positionService.updatePosition(id, positionDTO, authenticatedUser);
        return new ResponseEntity<>(updatedPosition, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a position", description = "Deletes a position by its ID")
    public ResponseEntity<Void> deletePosition(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        positionService.deletePosition(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employees")
    @Operation(summary = "Gets all employees on a position",
            description = "Retrieves all employees associated with a specific position")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesOnPosition(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        List<EmployeeDTO> employees = positionService.getAllEmployeesOnPosition(id, authenticatedUser);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
