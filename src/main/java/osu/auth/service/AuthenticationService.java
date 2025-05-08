package osu.auth.service;

import osu.auth.model.LoginResponse;
import osu.auth.model.LoginUserDto;
import osu.auth.model.RegisterUserDto;
import osu.user.model.User;

public interface AuthenticationService {
    User signup(RegisterUserDto input);
    User authenticate(LoginUserDto input);
    LoginResponse refreshToken(String refreshToken);
    boolean resetPassword(String email, String currentPassword, String newPassword);
    void generateAndSendPassword(String email);
}