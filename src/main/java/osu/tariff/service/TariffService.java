package osu.tariff.service;

import osu.tariff.model.TariffDTO;
import osu.user.model.User;

import java.util.List;

public interface TariffService {
    TariffDTO createTariff(TariffDTO tariffDTO, User authenticatedUser);
    TariffDTO updateTariff(Long id, TariffDTO tariffDTO, User authenticatedUser);
    void deleteTariff(Long id, User authenticatedUser);
    TariffDTO getTariffById(Long id, User authenticatedUser);
    List<TariffDTO> getAllTariffs(User authenticatedUser);
}