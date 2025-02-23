package osu.employee.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import osu.user.model.UserDto;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EmployeeDTO {

    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Size(max = 100, message = "Title before name must not exceed 100 characters")
    private String titleBeforeName;

    @Size(max = 100, message = "Title after name must not exceed 100 characters")
    private String titleAfterName;

    @NotNull(message = "Contract start date is required")
    private Date contractStart;

    @FutureOrPresent(message = "Contract end date must be in the future or today")
    private Date contractEnd;

    @NotNull(message = "Workload percentage is required")
    @DecimalMin(value = "0.0", message = "Workload percentage must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Workload percentage cannot exceed 1.0")
    private Double workloadPercentage;

    @NotBlank(message = "Salary grade cannot be blank")
    private String salaryGrade;

    @NotNull(message = "Tariff amount is required")
    @Positive(message = "Tariff amount must be positive")
    private Double tariffAmount;

    @PositiveOrZero(message = "Performance bonus must not be negative")
    private Double performanceBonus;

    @FutureOrPresent(message = "performanceBonusEligibilityDate is required")
    private Date performanceBonusEligibilityDate;

    @Positive(message = "Gross salary must be positive")
    private Double grossSalary;

    private Set<Long> positionIds;
    private UserDto createdBy;
}
