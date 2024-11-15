package osu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {

    private String username;
    private Set<ProjectDTO> projects;
}
