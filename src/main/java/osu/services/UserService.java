package osu.services;

import osu.dto.auth.UserDto;
import osu.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    List<User> allUsers();
}
