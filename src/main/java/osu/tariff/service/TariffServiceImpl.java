package osu.tariff.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    @Override
    public TariffDTO createTariff(TariffDTO tariffDTO) {
        Tariff tariff = tariffMapper.toEntity(tariffDTO);
        Tariff savedTariff = tariffRepository.save(tariff);
        return tariffMapper.toDto(savedTariff);
    }

    @Override
    @Transactional
    public TariffDTO updateTariff(Long id, TariffDTO tariffDTO) {
        Tariff existingTariff = tariffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tariff not found"));

        tariffMapper.updateTariffFromDto(tariffDTO, existingTariff);

        Tariff updatedTariff = tariffRepository.save(existingTariff);

        // Update the associated Employees
        if (updatedTariff.getPositions() != null) {
            updatedTariff.getPositions().forEach(position -> {
                Employee employee = position.getEmployee();
                if (employee != null) {
                    employee.setTariffAmount(updatedTariff.getWageTariff());
                    employee.calculateGrossSalary();
                    employeeRepository.save(employee);
                }
            });
        }

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