package osu.tariff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import osu.tariff.mapper.TariffMapper;
import osu.tariff.model.Tariff;
import osu.tariff.model.TariffDTO;
import osu.tariff.repository.TariffRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;

    @Override
    public TariffDTO createTariff(TariffDTO tariffDTO) {
        Tariff tariff = tariffMapper.toEntity(tariffDTO);
        Tariff savedTariff = tariffRepository.save(tariff);
        return tariffMapper.toDto(savedTariff);
    }

    @Override
    public TariffDTO updateTariff(Long id, TariffDTO tariffDTO) {
        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));
        tariffMapper.updateTariffFromDto(tariffDTO, existingTariff);
        Tariff updatedTariff = tariffRepository.save(existingTariff);
        return tariffMapper.toDto(updatedTariff);
    }

    @Override
    public void deleteTariff(Long id) {
        tariffRepository.deleteById(id);
    }

    @Override
    public TariffDTO getTariffById(Long id) {
        Tariff tariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));
        return tariffMapper.toDto(tariff);
    }

    @Override
    public List<TariffDTO> getAllTariffs() {
        return tariffRepository.findAll().stream()
                .map(tariffMapper::toDto)
                .collect(Collectors.toList());
    }
}