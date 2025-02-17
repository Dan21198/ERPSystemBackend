package osu.contract.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ContractDTO {

    private String id;

    @NotBlank(message = "Order number cannot be blank")
    @Size(max = 50, message = "Order number must not exceed 50 characters")
    private String orderNumber;

    @NotBlank(message = "Workplace number cannot be blank")
    @Size(max = 50, message = "Workplace number must not exceed 50 characters")
    private String workplaceNumber;

    @NotBlank(message = "Source cannot be blank")
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;

    @NotBlank(message = "Type cannot be blank")
    @Size(max = 50, message = "Type must not exceed 50 characters")
    private String type;

    @NotBlank(message = "Order name cannot be blank")
    @Size(max = 100, message = "Order name must not exceed 100 characters")
    private String orderName;

    @NotNull(message = "Available amount is required")
    @Positive(message = "Available amount must be positive")
    private Double availableAmount;

    @NotNull(message = "Duration start date is required")
    private Date durationFrom;

    @NotNull(message = "Duration end date is required")
    private Date durationTo;
}