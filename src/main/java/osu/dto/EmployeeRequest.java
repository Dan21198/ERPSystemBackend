package osu.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EmployeeRequest {
    private Long personalNumber;
    private String firstName;
    private String lastName;
    private String title;  // Enum mapping to AcademicTitle
    private Date contractStart;
    private Date contractEnd;
    private Double workloadPercentage;
    private String salaryGrade;
    private Double tariffAmount;
    private Double performanceBonus;
    private Double grossSalary;
    private PositionRequest position;  // Position request DTO
}

