package com.example.obes.dao;

import com.example.obes.model.User.UserCommon;
import com.example.obes.model.User.UserInstitutional;

public class LoginSessionManager {
    private static LoginSessionManager instance;
    private UserCommon currentUserCommon;
    private UserInstitutional currentUserInstitutional;

    private LoginSessionManager() {
        this.currentUserCommon = null;
        this.currentUserInstitutional = null;
    }

    public static LoginSessionManager getInstance() {
        if (instance == null) {
            instance = new LoginSessionManager();
        }
        return instance;
    }

    public UserCommon getCurrentUserCommon() {
        return this.currentUserCommon;
    }

    public UserInstitutional getCurrentUserInstitutional() {
        return this.currentUserInstitutional;
    }

    public void logout() {
        this.currentUserCommon = null;
        this.currentUserInstitutional = null;
    }

    public void loginUserCommon(UserCommon user) {
        this.currentUserCommon = user;
    }

    public void loginUserInstitutional(UserInstitutional user) {
        this.currentUserInstitutional = user;
    }
}
