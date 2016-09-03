package lv.mmm.services;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import lv.mmm.repos.LoanRepository;
import lv.mmm.repos.UserRepository;
import lv.mmm.services.defaultValueAppliers.LoanDefaultValueApplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final CountryResolveService countryResolveService;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       UserRepository userRepository,
                       CountryResolveService countryResolveService) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.countryResolveService = countryResolveService;
    }

    public Loan apply(Loan loan, String ip) {
        LoanDefaultValueApplier.apply(loan);
        loan.setApplicationCountry(countryResolveService.getCountryCodeByIP(ip));
        loan.setUser(this.resolveLoanUser(loan.getUser()));
        loanRepository.save(loan);
        return loan;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.getAllLoans();
    }

    public List<Loan> getByUser(User user) {
        return loanRepository.searchByUser(user);
    }

    private User resolveLoanUser(User loanQueryUser) {
        User exampleUser = new User();
        exampleUser.setPersonalId(loanQueryUser.getPersonalId());
        List<User> existingUser = userRepository.searchUsersByExample(exampleUser);
        if (existingUser.isEmpty()) {
            return userRepository.saveUser(loanQueryUser);
        } else {
            return existingUser.get(0);
        }
    }

}
