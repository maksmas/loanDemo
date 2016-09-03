package lv.mmm.rest;

import lv.mmm.domain.User;
import lv.mmm.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/blacklist")
public class UserBlacklistController {
    private final UserService userService;

    public UserBlacklistController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllBlacklistedUsers() {
        return userService.getAllBlacklistedUsers();
    }

    @GetMapping("/add/{userId}")
    public void addToBlacklist(@PathVariable Long userId) {
        userService.addToBlacklist(userId);
    }

    @GetMapping("/remove/{userId}")
    public void removeFromBlacklist(@PathVariable Long userId) {
        userService.removeFromBlacklist(userId);
    }
}
