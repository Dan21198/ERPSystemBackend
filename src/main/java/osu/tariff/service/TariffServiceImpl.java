package osu.tariff.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import osu.tariff.mapper.TariffMapper;
import osu.tariff.model.Tariff;
import osu.tariff.model.TariffDTO;
import osu.tariff.repository.TariffRepository;
import osu.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    @Override
    public TariffDTO createTariff(TariffDTO tariffDTO, User authenticatedUser) {
        Tariff tariff = tariffMapper.toEntity(tariffDTO);
        tariff.setCreatedBy(authenticatedUser);
        Tariff savedTariff = tariffRepository.save(tariff);
        return tariffMapper.toDto(savedTariff);
    }

    @Override
    @Transactional
    public TariffDTO updateTariff(Long id, TariffDTO tariffDTO, User authenticatedUser) {
        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));

        if (!existingTariff.getCreatedBy().equals(authenticatedUser)) {
            throw new RuntimeException("Unauthorized to update this tariff");
        }

        tariffMapper.updateTariffFromDto(tariffDTO, existingTariff);
        Tariff updatedTariff = tariffRepository.save(existingTariff);
        return tariffMapper.toDto(updatedTariff);
    }

    @Override
    public void deleteTariff(Long id, User authenticatedUser) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));

        if (!tariff.getCreatedBy().equals(authenticatedUser)) {
            throw new RuntimeException("Unauthorized to delete this tariff");
        }

        tariffRepository.deleteById(id);
    }

    @Override
    public TariffDTO getTariffById(Long id, User authenticatedUser) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));

         if (!tariff.getCreatedBy().equals(authenticatedUser)) {
             throw new RuntimeException("Unauthorized to view this tariff");
         }

        return tariffMapper.toDto(tariff);
    }

    @Override
    public List<TariffDTO> getAllTariffs(User authenticatedUser) {
        List<Tariff> tariffs = tariffRepository.findByCreatedBy(authenticatedUser);

        return tariffs.stream()
                .map(tariffMapper::toDto)
                .collect(Collectors.toList());
    }
}