package osu.employee.service;

import osu.assignment.model.Assignment;
import osu.performanceBonus.model.PerformanceBonus;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SalaryCalculator {

    public double calculateGrossSalary(Collection<Assignment> assignments) {
        double tariffAmount = calculateCurrentTariffAmount(assignments);
        double activeBonuses = calculateActivePerformanceBonuses(assignments);
        return tariffAmount + activeBonuses;
    }

    private double calculateCurrentTariffAmount(Collection<Assignment> assignments) {
        return assignments.stream()
                .filter(Assignment::isActive)
                .mapToDouble(assignment -> assignment.getTariff().getWageTariff()
                        * (assignment.getAllocatedTimePercentage() / 100.0))
                .sum();
    }

    private double calculateActivePerformanceBonuses(Collection<Assignment> assignments) {
        return assignments.stream()
                .filter(Assignment::isActive)
                .flatMap(assignment -> assignment.getPerformanceBonuses().stream())
                .filter(PerformanceBonus::getIsActive)
                .mapToDouble(PerformanceBonus::getAmount)
                .sum();
    }

}