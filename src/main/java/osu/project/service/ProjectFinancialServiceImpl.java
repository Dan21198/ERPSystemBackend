package osu.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import osu.contract.model.Contract;
import osu.position.model.Position;
import osu.project.model.Project;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;

import java.util.Objects;

@Service
public class ProjectFinancialServiceImpl implements ProjectFinancialService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Double calculateTotalAmountSpent(Project project) {
        if (project == null) {
            return 0.0;
        }

        if (project.getId() != null) {
            Project managedProject = entityManager.find(Project.class, project.getId());
            if (managedProject != null) {
                Hibernate.initialize(managedProject.getPositions());
                project = managedProject;
            }
        }

        if (project.getPositions() == null || project.getPositions().isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Position position : project.getPositions()) {
            if (position != null) {
                Double amount = position.getTotalAmountSpent();
                total += (amount != null) ? amount : 0.0;
            }
        }

        return total;
    }

    @Override
    @Transactional
    public void updateTotalAmountSpent(Project project) {
        if (project != null) {
            Double totalAmount = calculateTotalAmountSpent(project);
            project.setTotalAmountSpent(totalAmount);
        }
    }

    @Override
    @Transactional
    public Double calculateTotalAmountAllocated(Project project) {
        if (project == null) {
            return 0.0;
        }

        if (project.getId() != null) {
            Project managedProject = entityManager.find(Project.class, project.getId());
            if (managedProject != null) {
                Hibernate.initialize(managedProject.getContracts());
                project = managedProject;
            }
        }

        if (project.getContracts() == null || project.getContracts().isEmpty()) {
            return 0.0;
        }

        return project.getContracts().stream()
                .filter(Objects::nonNull)
                .mapToDouble(Contract::getAvailableAmount)
                .sum();
    }

    @Override
    @Transactional
    public void updateTotalAmountAllocated(Project project) {
        if (project != null) {
            Double totalAmount = calculateTotalAmountAllocated(project);
            project.setTotalAmountAllocated(totalAmount);
        }
    }

    @Override
    @Transactional
    public void synchronizeContractOrderNames(Project project) {
        if (project != null && project.getContracts() != null) {
            project.getContracts().forEach(Contract::synchronizeOrderName);
        }
    }
}