package osu.position.service;

import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.performanceBonus.model.PerformanceBonus;
import osu.position.model.Position;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@Service
public class PositionCalculationServiceImpl implements PositionCalculationService {

    @Override
    public Long calculateDurationInMonths(Position position) {
        if (position.getStartDate() != null && position.getEndDate() != null) {
            return ChronoUnit.MONTHS.between(position.getStartDate(), position.getEndDate());
        }
        return null;
    }

    @Override
    public Double calculateFte(Position position) {
        Long durationInMonths = calculateDurationInMonths(position);
        if (position.getAllocatedTimePercentage() != null && durationInMonths != null) {
            return (durationInMonths * position.getAllocatedTimePercentage()) / 100.0;
        }
        return null;
    }

    @Override
    public void updateTotalAmountSpent(Position position) {
        if (position.getAssignments() == null || position.getAssignments().isEmpty()) {
            position.setTotalAmountSpent(0.0);
            return;
        }

        double totalAmount = position.getAssignments().stream()
                .filter(a -> a.getTariff() != null)
                .mapToDouble(this::calculateAssignmentCost)
                .sum();

        position.setTotalAmountSpent(totalAmount);
    }

    @Override
    public double calculateAssignmentCost(Assignment assignment) {
        if (assignment.getTariff() == null
                || assignment.getAllocatedTimePercentage() == null
                || assignment.getStartDate() == null) {
            return 0.0;
        }

        LocalDate start = assignment.getStartDate();
        LocalDate end = LocalDate.now();

        if (assignment.getEndDate() != null && assignment.getEndDate().isBefore(end)) {
            end = assignment.getEndDate();
        }

        if (start.isAfter(end)) {
            return 0.0;
        }

        double monthlyCost = assignment.getTariff().getWageTariff() *
                (assignment.getAllocatedTimePercentage() / 100.0);

        double totalCost = 0.0;
        LocalDate current = start;

        while (!current.isAfter(end)) {
            YearMonth yearMonth = YearMonth.from(current);
            int daysInMonth = yearMonth.lengthOfMonth();
            LocalDate monthEnd = yearMonth.atEndOfMonth();
            LocalDate periodEnd = end.isBefore(monthEnd) ? end : monthEnd;

            long daysInPeriod = ChronoUnit.DAYS.between(current, periodEnd) + 1;
            double dailyRate = monthlyCost / daysInMonth;
            totalCost += dailyRate * daysInPeriod;

            current = periodEnd.plusDays(1);
        }

        double bonusCost = assignment.getPerformanceBonuses().stream()
                .filter(bonus -> bonus.getIsActive() &&
                        (bonus.getPerformanceBonusEligibilityDate() == null ||
                                !bonus.getPerformanceBonusEligibilityDate().isAfter(LocalDate.now())))
                .mapToDouble(PerformanceBonus::getAmount)
                .sum();

        return totalCost + bonusCost;
    }
}