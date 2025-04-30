package osu.position.service;

import org.springframework.stereotype.Service;
import osu.assignment.model.Assignment;
import osu.performanceBonus.model.PerformanceBonus;
import osu.position.model.Position;

import java.time.LocalDate;
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
        if (assignment.getTariff() == null || assignment.getAllocatedTimePercentage() == null) {
            return 0.0;
        }

        LocalDate effectiveStart = assignment.getStartDate();
        LocalDate effectiveEnd = assignment.isActive() ?
                (assignment.getEndDate() != null ? assignment.getEndDate() : LocalDate.now()) :
                assignment.getEndDate();

        if (effectiveStart == null || effectiveEnd == null || effectiveStart.isAfter(effectiveEnd)) {
            return 0.0;
        }

        long days = ChronoUnit.DAYS.between(effectiveStart, effectiveEnd) + 1;
        double avgDaysPerMonth = 30.44;
        double monthlyCost = assignment.getTariff().getWageTariff() *
                (assignment.getAllocatedTimePercentage() / 100.0);

        double bonusCost = assignment.getPerformanceBonuses().stream()
                .filter(PerformanceBonus::getIsActive)
                .mapToDouble(PerformanceBonus::getAmount)
                .sum();

        return ((monthlyCost / avgDaysPerMonth) * days) + bonusCost;
    }
}