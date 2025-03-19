package osu.assignment.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import osu.assignment.repository.AssignmentRepository;
import java.time.LocalDate;

@Component
public class AssignmentStatusScheduler {

    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentStatusScheduler(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAssignmentStatuses() {
        LocalDate now = LocalDate.now();
        assignmentRepository.findAll().forEach(assignment -> {
            boolean isActive = (assignment.getStartDate() != null && !now.isBefore(assignment.getStartDate())) &&
                    (assignment.getEndDate() == null || now.isBefore(assignment.getEndDate()));
            assignment.setActive(isActive);
            assignmentRepository.save(assignment);
        });
    }
}