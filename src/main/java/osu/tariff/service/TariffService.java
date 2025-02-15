package osu.tariff.service;

import osu.tariff.model.TariffDTO;

import java.util.List;

public interface TariffService {
    TariffDTO createTariff(TariffDTO tariffDTO);
    TariffDTO updateTariff(Long id, TariffDTO tariffDTO);
    void deleteTariff(Long id);
    TariffDTO getTariffById(Long id);
    List<TariffDTO> getAllTariffs();
}
