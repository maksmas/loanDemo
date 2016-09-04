package lv.mmm.services;

import lv.mmm.domain.User;
import lv.mmm.testUtils.UserBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    private User user1 = new UserBuilder().firstName("Super").lastName("Man").personalId("111111-11111").build();
    private User user2 = new UserBuilder().firstName("Iron").lastName("Man").personalId("111111-11112").build();
    private User user3 = new UserBuilder().firstName("Spider").lastName("Man").personalId("111111-11113").build();

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        Arrays.asList(user1,user2,user3).forEach(userService::addUser);
    }

    @After
    public void tearDown() throws Exception {
        userService.deleteAllUsers();
    }

    @Test
    public void shouldCreate3Users() throws Exception {
        Assert.assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    public void shouldFindUserById() {
        Assert.assertEquals(user1, userService.getUserById(user1.getId()).get());
    }

    @Test
    public void shouldDeleteUser() {
        userService.deleteUser(user1.getId());
        List<User> allUsers = userService.getAllUsers();
        Assert.assertEquals(2, allUsers.size());
        Assert.assertFalse(allUsers.contains(user1));
    }

    @Test
    public void blacklistTest() {
        userService.addToBlacklist(user1.getId());
        userService.addToBlacklist(user2.getId());
        List<User> blacklistedUsers = userService.getAllBlacklistedUsers();
        Assert.assertEquals(2, blacklistedUsers.size());
        Assert.assertFalse(blacklistedUsers.contains(user3));
        userService.removeFromBlacklist(user1.getId());
        blacklistedUsers = userService.getAllBlacklistedUsers();
        Assert.assertEquals(1, blacklistedUsers.size());
        Assert.assertFalse(blacklistedUsers.contains(user1));
    }

    @Test
    public void userSearchTest() {
        User userExample1 = new UserBuilder().firstName("Super").build();
        User userExample2 = new UserBuilder().lastName("Man").build();
        User userExample3 = new UserBuilder().personalId("111111-11111").build();
        User userExample4 = new UserBuilder().firstName("ron").build();
        User userExample5 = new UserBuilder().firstName("Iron").lastName("Man").build();
        User userExample6 = new UserBuilder().lastName("hhhh").build();

        Assert.assertEquals(1, userService.searchUsersByExample(userExample1).size());
        Assert.assertEquals(3, userService.searchUsersByExample(userExample2).size());
        Assert.assertEquals(1, userService.searchUsersByExample(userExample3).size());
        Assert.assertEquals(1, userService.searchUsersByExample(userExample4).size());
        Assert.assertEquals(1, userService.searchUsersByExample(userExample5).size());
        Assert.assertEquals(0, userService.searchUsersByExample(userExample6).size());
    }

    @Test
    public void userFirstNameValidationTest() {
        User userWithoutFirstName = new UserBuilder().lastName("ASD").personalId("13213").build();
        try {
            userService.addUser(userWithoutFirstName);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("User first name is mandatory", e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void userLastNameValidationTest() {
        User userWithoutLastName = new UserBuilder().firstName("ASD").personalId("13213").build();
        try {
            userService.addUser(userWithoutLastName);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("User last name is mandatory", e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void userPersonalIdValidationTest() {
        User userWithoutPersonalId = new UserBuilder().firstName("ASD").lastName("SDDD").build();
        try {
            userService.addUser(userWithoutPersonalId);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("User personal id is mandatory", e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void shouldNotAllowToCreateUsersWithSamePersonalId() {
        try {
            userService.addUser(user1);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("User with personal id: " + user1.getPersonalId() + " already exists",e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void shouldNotAllowToUpdateUsersWithSamePersonalId() {
        User updatedUser = new UserBuilder().firstName("Unique").lastName("Unique").personalId(user2.getPersonalId()).build();
        try {
            userService.updateUser(user1.getId(), updatedUser);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("User with personal id: " + user2.getPersonalId() + " already exists",e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void shouldNotAllowToSaveUserWithWrongName() {
        User wrongNamesUser = new UserBuilder().firstName("A").lastName("B").fullName("C").personalId("ABC").build();
        try {
            userService.addUser(wrongNamesUser);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Specified user full name doesn't much firstName + lastName", e.getMessage());
        } catch (Throwable t) {
            fail();
        }
    }

    @Test
    public void shouldUpdateUser() {
        User updatedUser = new UserBuilder().firstName("Garrosh").lastName("Hellscream").personalId("11111111111").build();
        userService.updateUser(user1.getId(), updatedUser);
        Assert.assertEquals(updatedUser, userService.getUserById(user1.getId()).get());
        Assert.assertEquals(3, userService.getAllUsers().size());
    }


}