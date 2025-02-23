package osu.project.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import osu.project.enums.ProjectStatus;
import osu.project.model.Project;
import osu.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProjectStatusUpdateService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectStatusUpdateService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void updateProjectStatuses() {
        Date currentDate = new Date();
        List<Project> projects = projectRepository.findAll();

        for (Project project : projects) {
            if (currentDate.before(project.getProjectStart())) {
                project.setProjectStatus(ProjectStatus.NOT_STARTED);
            } else if (currentDate.after(project.getProjectStart()) && currentDate.before(project.getProjectEnd())) {
                project.setProjectStatus(ProjectStatus.IN_PROGRESS);
            } else if (currentDate.after(project.getProjectEnd())) {
                project.setProjectStatus(ProjectStatus.COMPLETED);
            }
            projectRepository.save(project);
        }
    }
}