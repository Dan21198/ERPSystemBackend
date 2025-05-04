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

        List<PerformanceBonus> activeBonuses = performanceBonusRepository.findByIsActiveTrue();

        for (PerformanceBonus bonus : activeBonuses) {
            if (bonus.shouldBeDeactivated()) {
                bonus.setIsActive(false);
                performanceBonusRepository.save(bonus);

                performanceBonusService.handleBonusDeactivation(bonus);
            }
        }
    }
}