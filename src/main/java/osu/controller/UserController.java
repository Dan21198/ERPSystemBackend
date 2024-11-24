package osu.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import osu.dto.auth.UserDto;
import osu.mapper.UserMapper;
import osu.model.User;
import osu.model.Project;
import osu.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @Operation(summary = "Create or update a user", description = "Creates a new user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    @Operation(summary = "Retrieve all users", description = "Gets a list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a user by ID", description = "Gets the details of a user by their ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates the details of an existing user by their ID")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by their ID")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/me")
    @Operation(summary = "Get the authenticated user", description = "Retrieves details of the currently authenticated user")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        UserDto userDto = new UserDto();

        userDto.setId(currentUser.getId());
        userDto.setUsername(currentUser.getUsername());
        userDto.setEmail(currentUser.getEmail());
        userDto.setIsActive(currentUser.getIsActive());

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/")
    @Operation(summary = "Retrieve all users (DTO format)", description = "Gets a list of all users with limited details in DTO format")
    public ResponseEntity<List<UserDto>> allUsers() {
        List<User> users = userService.allUsers();
        List<UserDto> userDtos = userMapper.usersToUserDtos(users);
        return ResponseEntity.ok(userDtos);
    }
}
