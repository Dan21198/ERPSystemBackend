package osu.assignment.mapper;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import osu.position.model.Position;
import osu.position.repository.PositionRepository;

@Component
@Named("PositionMapperHelper")
public class PositionMapperHelper {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionMapperHelper(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Named("mapPosition")
    public Position mapPosition(Long positionId) {
        if (positionId == null) {
            return null;
        }
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + positionId));
    }
}