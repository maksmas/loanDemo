package lv.mmm.services;

import lv.mmm.domain.User;
import lv.mmm.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(Long userId) {
        Optional<User> optUser = userRepository.getUserById(userId);
        return optUser.orElseThrow(() -> new IllegalArgumentException("User not found by id " + userId));
    }

    public User addUser(User user) {
        userRepository.saveUser(user);
        return user;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }

    public User updateUser(Long userId, User updatedUser) {
        updatedUser.setId(userId);
        userRepository.saveUser(updatedUser);
        return updatedUser;
    }
}
