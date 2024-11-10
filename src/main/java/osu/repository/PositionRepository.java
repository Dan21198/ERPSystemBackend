package osu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.model.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
