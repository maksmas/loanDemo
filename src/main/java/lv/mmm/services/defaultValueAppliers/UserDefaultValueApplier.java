package lv.mmm.services.defaultValueAppliers;

import lv.mmm.domain.User;
import lv.mmm.utils.UserUtils;

public class UserDefaultValueApplier {

    public static void apply(User user) {
        if (user.getFullName() == null) {
            user.setFullName(UserUtils.generateUserFullName(user));
        }
    }
}
