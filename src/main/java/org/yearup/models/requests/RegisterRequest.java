package org.yearup.models.requests;

import org.yearup.models.Profile;
import org.yearup.models.User;

public class RegisterRequest
{
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public User toUser()
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthorities("USER");
        return user;
    }

    public Profile toProfile(int userId)
    {
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        return profile;
    }

    // getters + setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
