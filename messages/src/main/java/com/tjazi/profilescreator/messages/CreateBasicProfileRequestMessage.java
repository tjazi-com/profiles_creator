package com.tjazi.profilescreator.messages;

/**
 * Created by Krzysztof Wasiak on 18/10/15.
 *
 * Create basic profiles, which should be enough to login to the account.
 * Required values: user name, user email, password.
 * Additional details will be provided via UpdateProfileWithDetailsRequestMessage
 * in Profiles service
 */


public class CreateBasicProfileRequestMessage {

    private String userName;
    private String userEmail;
    private String passwordHash;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
