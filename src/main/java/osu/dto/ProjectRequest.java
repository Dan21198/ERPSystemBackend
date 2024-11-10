package osu.dto;


import lombok.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProjectRequest {
    private Long registrationNumber;
    private String projectCode;
    private String projectName;
    private String projectStatus;  // Enum mapping to ProjectStatus
    private Date projectStart;
    private Date projectEnd;
    private Set<EmployeeRequest> employees;  // Employee request DTO
}