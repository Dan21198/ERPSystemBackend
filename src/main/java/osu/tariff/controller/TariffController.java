package osu.tariff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.tariff.model.TariffDTO;
import osu.tariff.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @PostMapping
    public ResponseEntity<TariffDTO> createTariff(@RequestBody TariffDTO tariffDTO) {
        TariffDTO createdTariff = tariffService.createTariff(tariffDTO);
        return ResponseEntity.ok(createdTariff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffDTO> updateTariff(@PathVariable Long id, @RequestBody TariffDTO tariffDTO) {
        TariffDTO updatedTariff = tariffService.updateTariff(id, tariffDTO);
        return ResponseEntity.ok(updatedTariff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(@PathVariable Long id) {
        tariffService.deleteTariff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffDTO> getTariffById(@PathVariable Long id) {
        TariffDTO tariffDTO = tariffService.getTariffById(id);
        return ResponseEntity.ok(tariffDTO);
    }

    @GetMapping
    public ResponseEntity<List<TariffDTO>> getAllTariffs() {
        List<TariffDTO> tariffs = tariffService.getAllTariffs();
        return ResponseEntity.ok(tariffs);
    }
}