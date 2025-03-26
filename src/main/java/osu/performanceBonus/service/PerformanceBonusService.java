package osu.performanceBonus.service;

import osu.performanceBonus.model.PerformanceBonusDTO;

public interface PerformanceBonusService {
    PerformanceBonusDTO addBonus(Long assignmentId, PerformanceBonusDTO bonusDTO);
    void removeBonus(Long assignmentId, Long bonusId);
    PerformanceBonusDTO updateBonus(Long assignmentId, Long bonusId, PerformanceBonusDTO bonusDTO);
}