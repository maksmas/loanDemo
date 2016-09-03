package lv.mmm.repos;

import lv.mmm.domain.User;
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

}
