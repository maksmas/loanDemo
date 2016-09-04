package lv.mmm.services;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import lv.mmm.repos.LoanRepository;
import lv.mmm.services.defaultValueAppliers.LoanDefaultValueApplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final CountryResolveService countryResolveService;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       UserService userService,
                       CountryResolveService countryResolveService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.countryResolveService = countryResolveService;
    }

    public Loan apply(Loan loan, String ip) {
        LoanDefaultValueApplier.apply(loan);
        loan.setApplicationCountry(countryResolveService.getCountryCodeByIP(ip));
        loan.setUser(this.resolveLoanUser(loan.getUser()));
        return this.apply(loan);
    }

    private Loan apply(Loan loan) {
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.getAllLoans();
    }

    public List<Loan> getByUser(User user) {
        return loanRepository.searchByUser(user);
    }

    public void deleteAllLoans() {
        loanRepository.deleteAllLoans();
    }

    private User resolveLoanUser(User loanQueryUser) {
        if (loanQueryUser == null) {
            throw new IllegalArgumentException("Unable to resolve/create user by provided query arguments");
        }
        User exampleUser = new User();
        exampleUser.setPersonalId(loanQueryUser.getPersonalId());
        List<User> existingUser = userService.searchUsersByExample(exampleUser);
        if (existingUser.isEmpty()) {
            return userService.addUser(loanQueryUser);
        } else {
            return existingUser.get(0);
        }
    }

}
