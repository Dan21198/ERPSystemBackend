package osu.user.service;

import org.springframework.beans.BeanUtils;
import osu.exception.RecordNotFoundException;
import org.springframework.stereotype.Service;
import osu.user.model.User;
import osu.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with ID " + id + " not found"));

        BeanUtils.copyProperties(userDetails, existingUser, "id");

        Optional.ofNullable(userDetails.getIsActive()).ifPresent(existingUser::setIsActive);

        return userRepository.save(existingUser);
    }


    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with ID " + id + " not found"));

        userRepository.delete(existingUser);
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }
}