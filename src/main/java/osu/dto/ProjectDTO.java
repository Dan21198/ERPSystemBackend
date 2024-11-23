package osu.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import osu.dto.auth.UserDto;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProjectDTO {
    private Long id;
    private String projectCode;
    private String projectName;
    private String projectStatus;
    private Date projectStart;
    private Date projectEnd;
    private Integer employeeCount;
    private Set<EmployeeDTO> employees;
    private Set<UserDto> users;
}