package osu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import osu.dto.auth.UserDto;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private Date contractStart;
    private Date contractEnd;
    private Double workloadPercentage;
    private String salaryGrade;
    private Double tariffAmount;
    private Double performanceBonus;
    private Double grossSalary;
    private PositionDTO position;
    private UserDto createdBy;
}

