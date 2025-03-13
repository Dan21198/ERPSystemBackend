package osu.project.model;

import lombok.ToString;
import lombok.Value;
import osu.contract.model.ContractDTO;
import osu.project.enums.ProjectStatus;

import java.util.Date;
import java.util.Set;

@Value
@ToString
public class ProjectContractDTO {

    Long projectId;
    String projectCode;
    String projectName;
    ProjectStatus projectStatus;
    Date projectStart;
    Date projectEnd;
    Set<ContractDTO> contracts;
}