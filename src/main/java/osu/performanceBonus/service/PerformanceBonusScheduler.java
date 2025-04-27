package osu.performanceBonus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.repository.PerformanceBonusRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceBonusScheduler {

    private final PerformanceBonusRepository performanceBonusRepository;
    private final PerformanceBonusService performanceBonusService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void checkPerformanceBonusesValidity() {
        log.info("Starting scheduled performance bonuses check");

        List<PerformanceBonus> activeBonuses = performanceBonusRepository.findByIsActiveTrue();
        int deactivatedCount = 0;

        for (PerformanceBonus bonus : activeBonuses) {
            if (bonus.shouldBeDeactivated()) {
                bonus.setIsActive(false);
                performanceBonusRepository.save(bonus);

                performanceBonusService.handleBonusDeactivation(bonus);

                deactivatedCount++;
                log.debug("Deactivated bonus ID: {}, for assignment ID: {}",
                        bonus.getId(), bonus.getAssignment().getId());
            }
        }

        log.info("Completed scheduled performance bonuses check. Deactivated: {}", deactivatedCount);
    }
}