package osu.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.tariff.model.Tariff;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
}
