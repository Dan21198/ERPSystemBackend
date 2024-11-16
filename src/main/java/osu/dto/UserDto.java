package osu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;
    private Set<Long> projectIds;
}