package osu.position.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.position.service.PositionService;
import osu.position.model.PositionDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    @Operation(summary = "Create a position", description = "Creates a new position")
    public ResponseEntity<PositionDTO> createPosition(@RequestBody PositionDTO positionDTO) {
        PositionDTO createdPosition = positionService.createPosition(positionDTO);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a position", description = "Retrieves a position by its ID")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable Long id) {
        Optional<PositionDTO> position = positionService.getPosition(id);
        return position.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "Get all positions", description = "Retrieves all positions")
    public ResponseEntity<List<PositionDTO>> getAllPositions() {
        List<PositionDTO> positions = positionService.getAllPositions();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a position", description = "Updates an existing position by its ID")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        PositionDTO updatedPosition = positionService.updatePosition(id, positionDTO);
        return updatedPosition != null
                ? new ResponseEntity<>(updatedPosition, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a position", description = "Deletes a position by its ID")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
