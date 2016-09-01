package lv.mmm.rest;

import lv.mmm.domain.User;
import lv.mmm.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    private final UserRepository userRepository;

    @Autowired
    public HelloController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String index() {
        List<User> users = userRepository.getAllUsers();
        StringBuilder sb = new StringBuilder();
        sb.append(users.size());
        sb.append(":");
        users.forEach(sb::append);
        return sb.toString();
    }
}
