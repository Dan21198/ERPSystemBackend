package osu.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import osu.auth.model.LoginResponse;
import osu.auth.model.LoginUserDto;
import osu.auth.model.RegisterUserDto;
import osu.user.model.User;
import osu.user.repository.UserRepository;
import java.util.Random;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private static final int PASSWORD_LENGTH = 10;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final Random random = new Random();


    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
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
            throw new RuntimeException("Refresh token has expired");
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

    @Override
    public boolean resetPassword(String email, String currentPassword, String newPassword) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, currentPassword)
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Current password is incorrect");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    @Override
    public void generateAndSendPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email does not exist"));

        String generatedPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(generatedPassword));

        userRepository.save(user);
        emailService.sendGeneratedPasswordEmail(email, generatedPassword);
    }

    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARS.length());
            password.append(CHARS.charAt(index));
        }
        return password.toString();
    }
}