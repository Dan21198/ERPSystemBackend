package osu.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LoginUserDto {

    @NotBlank(message = "Username or email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
