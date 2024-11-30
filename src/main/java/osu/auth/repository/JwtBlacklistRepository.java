package osu.auth.repository;

import osu.auth.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);
    boolean existsByToken(String token);

}
