package lv.mmm.utils;

import lv.mmm.domain.Loan;
import lv.mmm.services.LoanService;
import lv.mmm.testUtils.LoanBuilder;
import lv.mmm.testUtils.UserBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test2.properties")
public class LoanServiceCallLimiterTest {
    private final Loan loan = new LoanBuilder().amount(500).term(61).
            user(new UserBuilder().firstName("Robin").lastName("Hood").personalId("111111-11111").build()).build();

    @Autowired
    private LoanService loanService;

    @Test
    public void shouldFailForManyRequests() {
        try {
            for (int i = 0; i < 5; i++) {
                loanService.apply(loan, "8.8.8.8");
            }
            fail();
        } catch (AccessDeniedException e) {
            Assert.assertEquals("Limit for loan apply service calls for US country is exceeded. Pleas wait", e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }
}