package osu.contract.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContractDTO {
    private String id;

    @NotBlank(message = "Contract type cannot be blank")
    @Size(max = 50, message = "Contract type must not exceed 50 characters")
    private String type;

    @NotNull(message = "Available amount is required")
    @Positive(message = "Available amount must be positive")
    private Double availableAmount;

    @NotBlank(message = "Workplace number cannot be blank")
    private String workplaceNumber;

    @NotBlank(message = "Source cannot be blank")
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @NotBlank(message = "Duration cannot be blank")
    private String duration;
}
