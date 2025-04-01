package osu.tariff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import osu.tariff.model.TariffDTO;
import osu.tariff.service.TariffService;
import osu.user.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @PostMapping
    public ResponseEntity<TariffDTO> createTariff(
            @RequestBody TariffDTO tariffDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        TariffDTO createdTariff = tariffService.createTariff(tariffDTO, authenticatedUser);
        return ResponseEntity.ok(createdTariff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TariffDTO> updateTariff(
            @PathVariable Long id,
            @RequestBody TariffDTO tariffDTO,
            @AuthenticationPrincipal User authenticatedUser) {
        TariffDTO updatedTariff = tariffService.updateTariff(id, tariffDTO, authenticatedUser);
        return ResponseEntity.ok(updatedTariff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTariff(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        tariffService.deleteTariff(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffDTO> getTariffById(
            @PathVariable Long id,
            @AuthenticationPrincipal User authenticatedUser) {
        TariffDTO tariffDTO = tariffService.getTariffById(id, authenticatedUser);
        return ResponseEntity.ok(tariffDTO);
    }

    @GetMapping
    public ResponseEntity<List<TariffDTO>> getAllTariffs(
            @AuthenticationPrincipal User authenticatedUser) {
        List<TariffDTO> tariffs = tariffService.getAllTariffs(authenticatedUser);
        return ResponseEntity.ok(tariffs);
    }
}