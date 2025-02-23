package osu.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.auth.model.LoginResponse;
import osu.auth.model.LoginUserDto;
import osu.auth.model.RegisterUserDto;
import osu.auth.service.AuthenticationService;
import osu.auth.service.JwtService;
import osu.auth.service.TokenBlacklistService;
import osu.user.mapper.UserMapper;
import osu.user.model.User;
import osu.user.model.UserDto;

import java.util.Map;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
                                    UserMapper userMapper, TokenBlacklistService tokenBlacklistService1) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.tokenBlacklistService = tokenBlacklistService1;
    }

    @PostMapping("/signup")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user in the system with the provided details and returns the created user"
    )
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        UserDto registerResponse = userMapper.toDto(registeredUser);
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user with their credentials and returns a JWT token with an expiration time"
    )
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);
        long expirationTime = jwtService.getExpirationTime();

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(expirationTime)
                .setRefreshToken(refreshToken);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh JWT token",
            description = "Refreshes the JWT token using a valid refresh token"
    )
    public ResponseEntity<?> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = "{\"refreshToken\": \"token\"}" // Example directly added here
                            )
                    )
            )
            @RequestBody Map<String, String> payload) {
        try {
            String refreshToken = payload.get("refreshToken");

            if (refreshToken == null || !jwtService.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token type");
            }

            LoginResponse loginResponse = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Logs out the user by invalidating the JWT token on the server"
    )
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean isLoggedOut = tokenBlacklistService.blacklistToken(token);
        if (isLoggedOut) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
