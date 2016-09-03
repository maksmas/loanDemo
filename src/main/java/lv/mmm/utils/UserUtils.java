package lv.mmm.utils;

import lv.mmm.domain.User;

public class UserUtils {

    public static String generateUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
