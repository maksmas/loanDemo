package lv.mmm.testUtils;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;

import java.math.BigDecimal;

public class LoanBuilder {
    private BigDecimal amount = null;
    private Integer term = null;
    private User user = null;

    public LoanBuilder amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LoanBuilder amount(Integer amount) {
        this.amount(BigDecimal.valueOf(amount));
        return this;
    }

    public LoanBuilder term(Integer term) {
        this.term = term;
        return this;
    }

    public LoanBuilder user(User user) {
        this.user = user;
        return this;
    }

    public Loan build() {
        Loan loan = new Loan();
        loan.setTerm(term);
        loan.setAmount(amount);
        loan.setUser(user);
        return loan;
    }
}
