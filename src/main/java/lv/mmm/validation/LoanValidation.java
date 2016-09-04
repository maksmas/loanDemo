package lv.mmm.validation;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import lv.mmm.repos.UserRepository;
import lv.mmm.utils.LoanServiceCallLimiter;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Aspect
@Component
public class LoanValidation {
    private final UserRepository userRepository;
    private final LoanServiceCallLimiter loanServiceCallLimiter;

    @Autowired
    public LoanValidation(UserRepository userRepository, LoanServiceCallLimiter loanServiceCallLimiter) {
        this.userRepository = userRepository;
        this.loanServiceCallLimiter = loanServiceCallLimiter;
    }

    @Pointcut("execution(* lv.mmm.repos.LoanRepository.*(..)) && args(..,loan)")
    public void loanRepositoryMethod(Loan loan) {
    }

    @Before(value = "loanRepositoryMethod(loan) && @annotation(lv.mmm.validation.rules.MandatoryFieldsRule)", argNames = "loan")
    public void mandatoryFieldsShouldBeSpecified(Loan loan) {
        if (loan.getAmount() == null) {
            throw new IllegalArgumentException("Loan amount is mandatory");
        } else if (loan.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("Loan amount must be positive number");
        }
        if (loan.getTerm() == null) {
            throw new IllegalArgumentException("Loan term is mandatory");
        } else if (loan.getTerm().compareTo(0) < 1) {
            throw new IllegalArgumentException("Loan term must be positive number");
        }
        if (loan.getUser() == null || loan.getUser().getId() == null) {
            throw new IllegalArgumentException("Unable to resolve/create user by provided query arguments");
        }
        if (loan.getApplicationDate() == null) {
            throw new IllegalArgumentException("Loan date is mandatory");
        }
        if (loan.getApplicationCountry() == null) {
            throw new IllegalArgumentException("Unable to resolve country");
        }
    }

    @Before(value = "loanRepositoryMethod(loan) && @annotation(lv.mmm.validation.rules.UserNotBlacklistedRule)", argNames = "loan")
    public void userShouldNotBeInBlacklist(Loan loan) {
        Optional<User> optActualUser = userRepository.getUserById(loan.getUser().getId());
        User actualUser = optActualUser.orElseThrow(() -> new IllegalArgumentException("Unable to resolve user"));
        if (actualUser.getInBlacklist()) {
            throw new IllegalArgumentException("Unable to proceed. User is in blacklist");
        }
    }

    @Before(value = "loanRepositoryMethod(loan) && @annotation(lv.mmm.validation.rules.LimitServiceCallRule)",argNames = "loan")
    public void countryShouldNotExceedItsLimit(Loan loan) {
        loanServiceCallLimiter.serviceCall(loan.getApplicationCountry());
    }
}
