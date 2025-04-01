package osu.position.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.position.model.Position;
import osu.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByIdAndCreatedBy(Long id, User createdBy);

    List<Position> findByCreatedBy(User createdBy);
}
