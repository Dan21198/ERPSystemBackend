package osu.user.service;

import osu.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
}
