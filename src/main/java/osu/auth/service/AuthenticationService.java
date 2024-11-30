package osu.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import osu.auth.model.LoginResponse;
import osu.auth.model.LoginUserDto;
import osu.auth.model.RegisterUserDto;
import osu.user.model.User;
import osu.user.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserDto input) {
        User user = new User()
                .setUsername(input.getUsername())
                .setEmail(input.getEmail())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole("USER")
                .setIsActive(true);

        return userRepository.save(user);
    }


    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            return null;
        }

        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newJwtToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        long expirationTime = jwtService.getExpirationTime();

        return new LoginResponse()
                .setToken(newJwtToken)
                .setExpiresIn(expirationTime)
                .setRefreshToken(newRefreshToken);
    }
}