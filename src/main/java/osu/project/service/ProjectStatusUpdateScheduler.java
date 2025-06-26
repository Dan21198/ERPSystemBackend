package osu.project.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import osu.project.model.Project;
import osu.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectStatusUpdateScheduler {

    private final ProjectRepository projectRepository;
    private final ProjectFinancialService projectFinancialService;

    @Autowired
    public ProjectStatusUpdateScheduler(ProjectRepository projectRepository,
                                        ProjectFinancialService projectFinancialService) {
        this.projectRepository = projectRepository;
        this.projectFinancialService = projectFinancialService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void updateProjectTotalAmountSpent() {
        List<Project> projects = projectRepository.findAll();

        for (Project project : projects) {
            projectFinancialService.updateTotalAmountSpent(project);
            projectRepository.save(project);
        }
    }
}