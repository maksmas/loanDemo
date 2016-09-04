package lv.mmm.services;

import lv.mmm.domain.Loan;
import lv.mmm.domain.User;
import lv.mmm.testUtils.LoanBuilder;
import lv.mmm.testUtils.UserBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
public class LoanServiceTest {
    private final Loan loan1 = new LoanBuilder().amount(500).term(61).
            user(new UserBuilder().firstName("Robin").lastName("Hood").personalId("111111-11111").build()).build();
    private final Loan loan2 = new LoanBuilder().amount(1000).term(30).
            user(new UserBuilder().firstName("Bruce").lastName("Wayne").personalId("010130-11112").build()).build();
    private final Loan loan3 = new LoanBuilder().amount(10000).term(Integer.MAX_VALUE).
            user(new UserBuilder().firstName("Luke").lastName("Skywalker").personalId("301299-11113").build()).build();
    private final Loan loan4 = new LoanBuilder().amount(1337).term(14).
            user(new UserBuilder().personalId("111111-11111").build()).build();

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    public void setUp() throws Exception {
        Arrays.asList(loan1,loan2,loan3,loan4).forEach(loan -> loanService.apply(loan, "8.8.8.8"));
    }

    @After
    public void tearDown() {
        loanService.deleteAllLoans();
        userService.deleteAllUsers();
    }

    @Test
    public void shouldCreate4Loans() throws Exception {
        setUp();
        Assert.assertEquals(4, loanService.getAllLoans().size());
    }

    @Test
    public void shouldCreate3Users() throws Exception {
        setUp();
        Assert.assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateLoanWhenUserIsBlacklisted() {
        User user = userService.addUser(new UserBuilder().firstName("Darth").lastName("Vader").personalId("123456-12345").build());
        userService.addToBlacklist(user.getId());
        try {
            loanService.apply(new LoanBuilder().amount(123).term(1).user(user).build(), "127.0.0.1");
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unable to proceed. User is in blacklist", e.getMessage());
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void shouldFailIfAmountNotSpecified() {
        User user = new UserBuilder().firstName("Darth").lastName("Vader").personalId("123456-12345").build();
        Loan loanWithoutAmount = new LoanBuilder().term(123).user(user).build();
        try {
            loanService.apply(loanWithoutAmount, "123.123.123.123");
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Loan amount is mandatory", e.getMessage());
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void shouldFailIfTermNotSpecified() {
        User user = new UserBuilder().firstName("Darth").lastName("Vader").personalId("123456-12345").build();
        Loan loanWithoutTerm = new LoanBuilder().amount(111).user(user).build();
        try {
            loanService.apply(loanWithoutTerm, "123.123.123.123");
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Loan term is mandatory", e.getMessage());
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void shouldFailIfUserNotSpecified() {
        Loan loanWithoutUser = new LoanBuilder().amount(123).term(111).build();
        try {
            loanService.apply(loanWithoutUser, "123.123.123.123");
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unable to resolve/create user by provided query arguments", e.getMessage());
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void searchByUserTest() throws Exception {
        setUp();
        User userExample1 = new UserBuilder().firstName("Robin").build();
        Assert.assertEquals(2, loanService.getByUser(userExample1).size());

        User userExample2 = new UserBuilder().firstName("asdf").build();
        Assert.assertEquals(0, loanService.getByUser(userExample2).size());

        User userExample3 = new UserBuilder().lastName("Hood").build();
        Assert.assertEquals(2, loanService.getByUser(userExample3).size());

        User userExample4 = new UserBuilder().personalId("301299-11113").build();
        Assert.assertEquals(1, loanService.getByUser(userExample4).size());

        User userExample5 = new UserBuilder().firstName("ruce").build();
        Assert.assertEquals(1, loanService.getByUser(userExample5).size());

        User userExample6 = new UserBuilder().firstName("Robin").lastName("Hood").build();
        Assert.assertEquals(2, loanService.getByUser(userExample6).size());
    }

}