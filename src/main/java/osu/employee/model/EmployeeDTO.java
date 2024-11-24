package osu.employee.model;

import lombok.*;
import osu.position.model.PositionDTO;
import osu.user.model.UserDto;

import java.util.Date;

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

