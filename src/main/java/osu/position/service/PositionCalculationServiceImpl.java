package osu.position.service;

import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.performanceBonus.model.PerformanceBonus;
import osu.position.model.Position;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
    public void checkAllowedSpentAmount(Position position) {
        if (position.getAllowedSpentAmount() == null) return;
        boolean isOver = position.getTotalAmountSpent() > position.getAllowedSpentAmount();
        position.setIsOverAllowedSpentAmount(isOver);
    }

    @Override
    public double calculateAssignmentCost(Assignment assignment) {
        if (!isValidAssignment(assignment)) {
            return 0.0;
        }

        LocalDate startDate = assignment.getStartDate();
        LocalDate endDate = determineEndDate(assignment);

        if (isStartAfterEnd(startDate, endDate)) {
            return 0.0;
        }

        return calculateBaseCost(assignment, startDate, endDate) + calculateBonusCost(assignment);
    }

    private boolean isValidAssignment(Assignment assignment) {
        return assignment.getTariff() != null
                && assignment.getAllocatedTimePercentage() != null
                && assignment.getStartDate() != null;
    }

    private boolean isStartAfterEnd(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate);
    }

    private LocalDate determineEndDate(Assignment assignment) {
        return Optional.ofNullable(assignment.getEndDate())
                .filter(endDate -> endDate.isBefore(LocalDate.now()))
                .orElse(LocalDate.now());
    }

    private double calculateBaseCost(Assignment assignment, LocalDate startDate, LocalDate endDate) {
        double monthlyCost = calculateMonthlyCost(assignment);
        double totalCost = 0.0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            YearMonth yearMonth = YearMonth.from(currentDate);
            LocalDate periodEnd = calculatePeriodEnd(yearMonth, endDate);

            totalCost += calculatePeriodCost(monthlyCost, yearMonth, currentDate, periodEnd);
            currentDate = periodEnd.plusDays(1);
        }

        return totalCost;
    }

    private LocalDate calculatePeriodEnd(YearMonth yearMonth, LocalDate endDate) {
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        return endDate.isBefore(monthEnd) ? endDate : monthEnd;
    }

    private double calculatePeriodCost(double monthlyCost, YearMonth yearMonth,
                                       LocalDate periodStart, LocalDate periodEnd) {
        int daysInMonth = yearMonth.lengthOfMonth();
        long daysInPeriod = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
        double dailyRate = monthlyCost / daysInMonth;
        return dailyRate * daysInPeriod;
    }

    private double calculateMonthlyCost(Assignment assignment) {
        return assignment.getTariff().getWageTariff() *
                (assignment.getAllocatedTimePercentage() / 100.0);
    }

    private double calculateBonusCost(Assignment assignment) {
        return assignment.getPerformanceBonuses().stream()
                .filter(this::isEligibleBonus)
                .mapToDouble(PerformanceBonus::getAmount)
                .sum();
    }

    private boolean isEligibleBonus(PerformanceBonus bonus) {
        LocalDate eligibilityDate = bonus.getPerformanceBonusEligibilityDate();

        if (eligibilityDate == null) {
            return true;
        }

        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        YearMonth bonusMonth = YearMonth.from(eligibilityDate);

        return bonusMonth.isBefore(currentMonth) || bonusMonth.equals(currentMonth);
    }
}