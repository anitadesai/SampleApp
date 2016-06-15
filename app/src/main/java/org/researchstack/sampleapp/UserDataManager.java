package org.researchstack.sampleapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anita on 6/10/2016.
 */
public class UserDataManager {

    // TODO: remove fake user info
    public static final String fakeEmail = "fake@gmail.com";
    public static final String fakeUser = "fakeuser";
    public static final String fakePass = "password";
    public static final User FAKE_USER = new User(fakeEmail, fakeUser, fakePass);

    public static final String TAG = "UserDataManager";

    private static UserDataManager instance;
    private Map<String, User> users;
    private User currentUser;

    public static UserDataManager getInstance() {
        return instance;
    }

    public void init(){
        instance = new UserDataManager();
        users = new HashMap<>();
        //signUp(fakeEmail, fakeUser, fakePass);
        //signIn(fakeUser, fakePass);
    }

    // add check for null user or withdrawn here
    public User getUser(String username){
        return users.get(username);
    }

    public void signUp(String email, String username, String password) {
        User newUser = new User(email, username, password);
        users.put(username, newUser);
    }

    public void signIn(String username, String password) {
        User user = users.get(username);

        if (user != null && !user.getWithdrawn() && user.getPassword().equals(password)) {
            user.setSignedIn(true);
            setCurrentUser(username);
        }
    }

    public void signOut() {
        if (currentUser != null && currentUser.getSignedIn()) {
            currentUser.setSignedIn(false);
            //currentUser = null;
        }
    }

    public void changePassword(String userId, String newPassword) {
        User user = users.get(userId);
        user.setPassword(newPassword);
    }

    public void changeEmail(String userId, String newEmail) {
        User user = users.get(userId);
        user.setEmail(newEmail);
    }

    public void enterPasscode(String userId, String passcode) {
        User user = users.get(userId);

        if (user.getPasscode().equals(passcode)) {
            user.setSignedIn(true);
        }
    }

    public void withdrawFromStudy(String reason) {
    currentUser.setWithdrawn(true, reason);
    }

    public User getCurrentUser(){
        //return currentUser;
        return FAKE_USER;
    }

    public void setCurrentUser(String username){
        //currentUser = getUser(username);
        currentUser = FAKE_USER;
        Log.i(TAG, "setCurrentUser: USER BEING CHANGED TO: " + username);
    }
}
