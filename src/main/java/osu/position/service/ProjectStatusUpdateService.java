package osu.position.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;

import java.util.List;

@Service
public class ProjectStatusUpdateService {

    private final PositionRepository positionRepository;

    @Autowired
    public ProjectStatusUpdateService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    @Transactional
    public void updatePositionTotalAmountSpent() {
        List<Position> positions = positionRepository.findAll();

        for (Position position : positions) {
            position.updateTotalAmountSpent();
            positionRepository.save(position);
        }
    }
}
