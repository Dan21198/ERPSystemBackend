package osu.services;

import osu.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);
}
