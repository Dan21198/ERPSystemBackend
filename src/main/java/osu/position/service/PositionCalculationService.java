package osu.position.service;

import osu.assignment.model.Assignment;
import osu.position.model.Position;

public interface PositionCalculationService {
    Long calculateDurationInMonths(Position position);
    Double calculateFte(Position position);
    void updateTotalAmountSpent(Position position);
    double calculateAssignmentCost(Assignment assignment);
}
