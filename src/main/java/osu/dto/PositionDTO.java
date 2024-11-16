package osu.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PositionDTO {
    private Long id;
    private String name;
    private Set<EmployeeDTO> employees;
}
