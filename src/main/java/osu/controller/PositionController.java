package osu.controller;

import osu.dto.PositionDTO;
import osu.model.Position;
import osu.services.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<PositionDTO> createPosition(@RequestBody Position position) {
        PositionDTO createdPosition = positionService.createPosition(position);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable Long id) {
        Optional<PositionDTO> position = positionService.getPosition(id);
        return position.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PositionDTO>> getAllPositions() {
        List<PositionDTO> positions = positionService.getAllPositions();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable Long id, @RequestBody Position position) {
        PositionDTO updatedPosition = positionService.updatePosition(id, position);
        return updatedPosition != null
                ? new ResponseEntity<>(updatedPosition, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }
}
