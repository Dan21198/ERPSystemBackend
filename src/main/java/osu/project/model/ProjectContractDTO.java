package osu.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import osu.contract.model.ContractDTO;
import osu.project.enums.ProjectStatus;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
public class ProjectContractDTO {

    private Long projectId;
    private String projectCode;
    private String projectName;
    private ProjectStatus projectStatus;
    private Date projectStart;
    private Date projectEnd;

    private Set<ContractDTO> contracts;
}
