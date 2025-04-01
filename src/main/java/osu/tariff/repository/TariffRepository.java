package osu.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.tariff.model.Tariff;
import osu.user.model.User;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    List<Tariff> findByCreatedBy(User createdBy);
}
