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
    public User getUserById(Long userId) {
        return userRepository.getUserById(userId).get();
    }

    @FirstLastNameMatchFullNameRule
    @MandatoryFieldsRule
    @PersonIdUniqueRule
    public User addUser(User user) {
        UserDefaultValueApplier.apply(user);
        userRepository.saveUser(user);
        return user;
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
        userRepository.saveUser(updatedUser);
        return updatedUser;
    }
}
