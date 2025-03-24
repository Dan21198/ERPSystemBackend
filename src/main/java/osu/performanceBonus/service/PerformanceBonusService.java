package osu.performanceBonus.service;

import osu.performanceBonus.model.PerformanceBonusDTO;

public interface PerformanceBonusService {
    PerformanceBonusDTO addBonus(Long employeeId, PerformanceBonusDTO bonusDTO);
    void removeBonus(Long employeeId, Long bonusId);
    PerformanceBonusDTO updateBonus(Long employeeId, Long bonusId, PerformanceBonusDTO bonusDTO);
}
