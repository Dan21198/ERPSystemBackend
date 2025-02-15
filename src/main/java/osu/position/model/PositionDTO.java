package osu.position.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import osu.tariff.model.TariffDTO;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PositionDTO {
    private Long id;

    @NotBlank(message = "Position name cannot be blank")
    @Size(max = 100, message = "Position name must not exceed 100 characters")
    private String name;

    private Set<TariffDTO> tariffs;
}