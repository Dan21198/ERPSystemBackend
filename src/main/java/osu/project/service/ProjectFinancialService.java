package osu.project.service;

import osu.project.model.Project;

public interface ProjectFinancialService {
    Double calculateTotalAmountSpent(Project project);
    void updateTotalAmountSpent(Project project);
    Double calculateTotalAmountAllocated(Project project);
    void updateTotalAmountAllocated(Project project);
    void synchronizeContractOrderNames(Project project);
}
