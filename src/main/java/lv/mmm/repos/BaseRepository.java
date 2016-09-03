package lv.mmm.repos;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public abstract class BaseRepository<T> {
    private static final String DELETE_HQL_STR = "delete from :clazz where id = :id";
    protected SessionFactory sessionFactory;

    public BaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<T> getAll(Class clazz) {
        return getCurrentSession().createCriteria(clazz).list();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    protected List<T> searchByExample(T example) {
        if (example == null) {
            throw new NullPointerException("Example may not be null");
        }
        List<T> returnedList = getCurrentSession().createCriteria(example.getClass()).add(Example.create(example)).list();
        return returnedList == null ? Collections.emptyList() : returnedList;
    }

    protected  void deleteById(Class clazz, Long id) {
        //Sorry for this one. I'm new to hibernate.
        //FIXME replace on string obj
        Query query = getCurrentSession().createQuery(DELETE_HQL_STR.replace(":clazz", clazz.getName())).setLong("id", id);
        query.executeUpdate();
    }
}
