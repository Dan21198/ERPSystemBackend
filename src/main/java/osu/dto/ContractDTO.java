package osu.dto;

import lombok.Data;

@Data
public class ContractDTO {
    private String type;
    private Double availableAmount;
    private String workplaceNumber;
    private String source;
    private String duration;
}
