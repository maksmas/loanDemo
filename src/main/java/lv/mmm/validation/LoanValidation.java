package lv.mmm.validation;

import lv.mmm.repos.LoanRepository;
import lv.mmm.repos.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoanValidation {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public LoanValidation(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }


}
