package lv.mmm.rest;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import lv.mmm.services.LoanService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public Loan apply(@RequestBody Loan loan, HttpServletRequest request) {
        return loanService.apply(loan, request.getRemoteAddr());
    }

    @GetMapping
    public List<Loan> getAll() {
        return loanService.getAllLoans();
    }

    @GetMapping("/byUser")
    public List<Loan> getByUser(Long id, User user) {
        //In this case allow specifying user id
        user.setId(id);
        return loanService.getByUser(user);
    }

}
