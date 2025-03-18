package osu.assignment.mapper;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import osu.tariff.model.Tariff;
import osu.tariff.repository.TariffRepository;

@Component
@Named("TariffMapperHelper")
public class TariffMapperHelper {

    private final TariffRepository tariffRepository;

    @Autowired
    public TariffMapperHelper(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Named("mapTariff")
    public Tariff mapTariff(Long tariffId) {
        if (tariffId == null) {
            return null;
        }
        return tariffRepository.findById(tariffId)
                .orElseThrow(() -> new RuntimeException("Tariff not found with id: " + tariffId));
    }
}