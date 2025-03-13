package osu.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import osu.position.model.PositionDTO;
import osu.user.model.UserDto;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProjectDTO {
    private Long id;

    @NotBlank(message = "Project code cannot be blank")
    @Size(max = 20, message = "Project code must not exceed 20 characters")
    private String projectCode;

    @NotBlank(message = "Project name cannot be blank")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String projectName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String projectStatus;

    @NotNull(message = "Project start date cannot be null")
    private Date projectStart;

    @NotNull(message = "Project end date cannot be null")
    private Date projectEnd;

    private Set<PositionDTO> positions;
    private Set<UserDto> users;
}