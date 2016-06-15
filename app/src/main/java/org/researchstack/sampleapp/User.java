package org.researchstack.sampleapp;

/**
 * Created by Anita on 6/10/2016.
 */
public class User extends org.researchstack.skin.model.User {

    // skin user just has name- username in this case
    private String firstname;
    private String lastname;
    private String password;
    private String passcode;
    private String token;
    private Boolean signedIn;
    private Boolean withdrawn;
    private String withdrawReason;
    private int distressStreak;

    public User(String email, String username, String password) {
        setEmail(email);
        setName(username);
        setPassword(password);
        this.distressStreak = 0;
        this.withdrawn = false;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDistressStreak() {
        return distressStreak;
    }

    public void setDistressStreak(int distressStreak) {
        this.distressStreak = distressStreak;
    }

    public Boolean getWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(Boolean withdrawn, String reason) {
        this.withdrawn = withdrawn;
        this.withdrawReason = reason;
    }

}
