package com.example.obes.dao;

import com.example.obes.model.User.UserCommon;

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
    public boolean deleteUser(UserCommon user) {
        UserCommon userDelete = null;
        boolean removed = false;

        for( UserCommon u : listUsersCommon ){
            if( user.getId() == u.getId() ){
                userDelete = u;
                break;
            }
        }

        if( userDelete != null ){
            listUsersCommon.remove( userDelete );
            removed = true;

        }

        return removed;
    }

    public boolean editUser(UserCommon user) {
        UserCommon userEdit = null;
        boolean edited = false;

        for(UserCommon u : listUsersCommon){
            if(user.getId() == u.getId()){
                userEdit = u;
                edited = true;
                break;
            }
        }

        if(edited){
            userEdit.setName(user.getName());
            userEdit.setContact(user.getContact());
            userEdit.setEmail(user.getEmail());
            userEdit.setPassword(user.getPassword());
            userEdit.setCpf(user.getCpf());
            userEdit.setDateOfBirth(user.getDateOfBirth());
        }

        return edited;
    }
    public UserCommon getUserById(int idUser) {
        UserCommon user = null;

        for(UserCommon u : this.listUsersCommon){
            if( u.getId() == idUser ){
                user = u;
                break;
            }
        }

        return user;
    }
}

