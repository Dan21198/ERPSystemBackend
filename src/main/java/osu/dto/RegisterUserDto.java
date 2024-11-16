package osu.dto;

import lombok.*;

import java.util.Set;

@Data
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;
    private String role;
    private Boolean isActive;
    private Set<Long> projectIds;
    private Set<Long> employeeIds;
}