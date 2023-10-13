package com.example.obes.dao;

import com.example.obes.model.user.UserCommon;

import java.util.ArrayList;

public class UserCommonDAO {
    private ArrayList<UserCommon> listUsersCommon;
    private static UserCommonDAO instance;

    private UserCommonDAO() {
        this.listUsersCommon = new ArrayList<UserCommon>();
    }

    public static UserCommonDAO getInstance() {
        if(instance == null) {
            instance = new UserCommonDAO();
        }
        return instance;
    }

    public ArrayList<UserCommon> getListUsers() {
        return listUsersCommon;
    }

    public boolean addUser(UserCommon user) {
        this.listUsersCommon.add(user);
        return true;
    }

}
