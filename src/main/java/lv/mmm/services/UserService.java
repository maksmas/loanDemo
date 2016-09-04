package lv.mmm.services;

import lv.mmm.domain.User;
import lv.mmm.repos.UserRepository;
import lv.mmm.services.defaultValueAppliers.UserDefaultValueApplier;
import lv.mmm.validation.rules.EntityExistsRule;
import lv.mmm.validation.rules.FirstLastNameMatchFullNameRule;
import lv.mmm.validation.rules.MandatoryFieldsRule;
import lv.mmm.validation.rules.PersonIdUniqueRule;
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

    @EntityExistsRule
    public Optional<User> getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    @FirstLastNameMatchFullNameRule
    @MandatoryFieldsRule
    @PersonIdUniqueRule
    public User addUser(User user) {
        UserDefaultValueApplier.apply(user);
        return userRepository.saveUser(user);
    }

    @EntityExistsRule
    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @FirstLastNameMatchFullNameRule
    @MandatoryFieldsRule
    @EntityExistsRule
    @PersonIdUniqueRule
    public User updateUser(Long userId, User updatedUser) {
        UserDefaultValueApplier.apply(updatedUser);
        updatedUser.setId(userId);
        return userRepository.saveUser(updatedUser);
    }

    public List<User> getAllBlacklistedUsers() {
        User userExample = new User();
        userExample.setInBlacklist(true);
        return userRepository.searchUsersByExample(userExample);
    }

    @EntityExistsRule
    public void addToBlacklist(Long userId) {
        userRepository.manageBlacklistStatus(userId, true);
    }

    @EntityExistsRule
    public void removeFromBlacklist(Long userId) {
        userRepository.manageBlacklistStatus(userId, false);
    }

    public void deleteAllUsers() {
        userRepository.deleteAllUsers();
    }

    public List<User> searchUsersByExample(User userExample) {
        return userRepository.searchUsersByExample(userExample);
    }
}
