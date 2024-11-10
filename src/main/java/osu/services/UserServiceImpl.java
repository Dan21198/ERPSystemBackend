package osu.services;

import org.springframework.beans.BeanUtils;
import osu.model.User;
import osu.repository.UserRepository;
import osu.exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
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
}