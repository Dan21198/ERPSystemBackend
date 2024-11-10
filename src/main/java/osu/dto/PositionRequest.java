package osu.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PositionRequest {
    private Long id;
    private String name;
}
