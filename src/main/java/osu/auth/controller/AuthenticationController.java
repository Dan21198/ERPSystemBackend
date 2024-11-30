package osu.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import osu.auth.service.AuthenticationService;
import osu.auth.service.JwtService;
import osu.auth.model.LoginResponse;
import osu.auth.model.LoginUserDto;
import osu.auth.model.RegisterUserDto;
import osu.user.model.UserDto;
import osu.user.mapper.UserMapper;
import osu.user.model.User;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService,
                                    UserMapper userMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
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
        long expirationTime = jwtService.getExpirationTime();

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(expirationTime);

        return ResponseEntity.ok(loginResponse);
    }
}
