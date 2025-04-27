package osu.performanceBonus.service;

import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.user.model.User;

public interface PerformanceBonusService {
    PerformanceBonusDTO addBonus(Long assignmentId, PerformanceBonusDTO bonusDTO, User authenticatedUser);
    void removeBonus(Long assignmentId, Long bonusId, User authenticatedUser);
    PerformanceBonusDTO updateBonus(Long assignmentId, Long bonusId, PerformanceBonusDTO bonusDTO, User authenticatedUser);
    void handleBonusDeactivation(PerformanceBonus bonus);
    PerformanceBonusDTO deactivateBonus(Long assignmentId, Long bonusId, User authenticatedUser);
    PerformanceBonusDTO activateBonus(Long assignmentId, Long bonusId, User authenticatedUser);
}