package lv.mmm.repos;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@Transactional
public class LoanRepository extends BaseRepository<Loan> {

    public LoanRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void save(Loan loan) {
        getCurrentSession().saveOrUpdate(loan);
    }

    public List<Loan> getAllLoans() {
        return getAll(Loan.class);
    }

    public List<Loan> searchByUser(User user) {
        Criteria searchCriteria = getCurrentSession().createCriteria(Loan.class).createAlias("user", "u");
        if (user.getId() != null) {
            searchCriteria.add(Restrictions.eq("u.id", user.getId()));
        }
        if (!StringUtils.isEmpty(user.getFirstName())) {
            searchCriteria.add(Restrictions.ilike("u.firstName", user.getFirstName(), MatchMode.ANYWHERE));
        }
        if (!StringUtils.isEmpty(user.getLastName())) {
            searchCriteria.add(Restrictions.ilike("u.lastName", user.getLastName(), MatchMode.ANYWHERE));
        }
        if (!StringUtils.isEmpty(user.getFullName())) {
            searchCriteria.add(Restrictions.ilike("u.fullName", user.getFullName(), MatchMode.ANYWHERE));
        }
        if (!StringUtils.isEmpty(user.getPersonalId())) {
            searchCriteria.add(Restrictions.like("u.personalId", user.getPersonalId(), MatchMode.ANYWHERE));
        }
        return searchCriteria.list();
    }
}
