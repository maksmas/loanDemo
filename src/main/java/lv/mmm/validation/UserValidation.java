package lv.mmm.validation;

import lv.mmm.domain.User;
import lv.mmm.repos.UserRepository;
import lv.mmm.utils.UserUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class UserValidation {

    private final UserRepository userRepository;

    @Autowired
    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Pointcut("execution(* lv.mmm.services.UserService.*(..)) && args(..,user)")
    public void userServiceMethodPointcut(User user) {
    }

    @Before(value = "userServiceMethodPointcut(user) && @annotation(lv.mmm.validation.rules.FirstLastNameMatchFullNameRule)", argNames = "user")
    public void firstAndLastNameShouldMatchFullName(User user) {
        if (user.getFullName() != null) {
            String generatedFullName = UserUtils.generateUserFullName(user);
            if (!generatedFullName.equals(user.getFullName())) {
                //TODO move this to resource file
                throw new IllegalArgumentException("Specified user full name doesn't much firstName + lastName");
            }
        }
    }

    @Before(value = "userServiceMethodPointcut(user) && @annotation(lv.mmm.validation.rules.MandatoryFieldsRule)", argNames = "user")
    public void mandatoryFieldsShouldBeSpecified(User user) {
        if (StringUtils.isEmpty(user.getFirstName())) {
            throw new IllegalArgumentException("User first name is mandatory");
        }
        if (StringUtils.isEmpty(user.getLastName())) {
            throw new IllegalArgumentException("User last name is mandatory");
        }
        if (StringUtils.isEmpty(user.getPersonalId())) {
            throw new IllegalArgumentException("User personal id is mandatory");
        }
    }

    @Before("execution(* lv.mmm.services.UserService.*(..)) && args(userId,..) && @annotation(lv.mmm.validation.rules.EntityExistsRule)")
    public void userWithGivenIdShouldExist(Long userId) {
        if (!userRepository.getUserById(userId).isPresent()) {
            throw new IllegalArgumentException("Can't find user with id: " + userId);
        }
    }

    @Before(value = "userServiceMethodPointcut(user) && @annotation(lv.mmm.validation.rules.PersonIdUniqueRule)", argNames = "user")
    public void personIdShouldBeUniqueOnSave(User user) {
        User exampleUser = new User();
        exampleUser.setPersonalId(user.getPersonalId());
        List<User> usersWithSamePersonalId = userRepository.searchUsersByExample(exampleUser);
        if (!usersWithSamePersonalId.isEmpty()) {
            throw new IllegalArgumentException("User with personal id: " + user.getPersonalId() + " already exists");
        }
    }

    @Before(value = "execution(* lv.mmm.services.UserService.*(..)) && args(userId,user,..) && @annotation(lv.mmm.validation.rules.PersonIdUniqueRule)", argNames = "userId,user")
    public void personIdShouldBUniqueOnUpdate(Long userId, User user) {
        User exampleUser = new User();
        exampleUser.setId(userId);
        exampleUser.setPersonalId(user.getPersonalId());
        List<User> usersWithSamePersonalId = userRepository.searchUsersByExample(exampleUser);
        if (!usersWithSamePersonalId.isEmpty()) {
            throw new IllegalArgumentException("User with personal id: " + user.getPersonalId() + " already exists");
        }
    }
}
