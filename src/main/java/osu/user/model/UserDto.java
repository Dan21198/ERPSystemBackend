package osu.user.model;

import lombok.Data;


@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean isActive;
}