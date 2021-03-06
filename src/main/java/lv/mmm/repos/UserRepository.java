package lv.mmm.repos;

import lv.mmm.domain.User;
import lv.mmm.validation.rules.FirstLastNameMatchFullNameRule;
import lv.mmm.validation.rules.MandatoryFieldsRule;
import lv.mmm.validation.rules.PersonIdUniqueRule;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepository extends BaseRepository<User> {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> getAllUsers() {
        return getAll(User.class);
    }

    public User saveUser(User user) {
        getCurrentSession().saveOrUpdate(user);
        return user;
    }

    public List<User> searchUsersByExample(User userExample) {
        return searchByExample(userExample);
    }

    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(getCurrentSession().get(User.class, userId));
    }

    public void deleteUserById(Long userId) {
        deleteById(User.class, userId);
    }

    public void manageBlacklistStatus(Long userId, Boolean inBlackList) {
        String blacklistHql = "update User u set u.inBlacklist = :inBL where u.id = :userId";
        getCurrentSession().createQuery(blacklistHql).
                setBoolean("inBL", inBlackList).
                setLong("userId", userId).executeUpdate();
    }

    public void deleteAllUsers() {
        getCurrentSession().createQuery("DELETE FROM User").executeUpdate();
    }
}
