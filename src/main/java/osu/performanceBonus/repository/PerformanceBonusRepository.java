package osu.performanceBonus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.performanceBonus.model.PerformanceBonus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceBonusRepository extends JpaRepository<PerformanceBonus, Long> {
    Optional<PerformanceBonus> findByIdAndAssignmentId(Long bonusId, Long assignmentId);

    List<PerformanceBonus> findByIsActiveTrue();
}