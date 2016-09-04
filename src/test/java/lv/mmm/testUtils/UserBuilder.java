package lv.mmm.testUtils;

import lv.mmm.domain.User;

public class UserBuilder {
    private String firstName = null;
    private String lastName = null;
    private String fullName = null;
    private String personalId = null;
    private Boolean inBlacklist = null;
    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserBuilder personalId(String personalId){
        this.personalId = personalId;
        return this;
    }

    public UserBuilder inBlacklist(boolean inBlacklist){
        this.inBlacklist = inBlacklist;
        return this;
    }

    public User build() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPersonalId(personalId);
        user.setFullName(fullName);
        user.setInBlacklist(inBlacklist);
        return user;
    }

}
