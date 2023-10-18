package com.example.obes.dao;

import com.example.obes.model.User.UserInstitutional;

import java.util.ArrayList;

public class UserInstitutionalDAO implements IUserInstitutionalDAO {
    private ArrayList<UserInstitutional> listUsersInstitutional;
    private static UserInstitutionalDAO instance;

    private UserInstitutionalDAO() {
        this.listUsersInstitutional = new ArrayList<UserInstitutional>();
    }

    public static UserInstitutionalDAO getInstance() {
        if(instance == null) {
            instance = new UserInstitutionalDAO();
        }
        return instance;
    }

    public ArrayList<UserInstitutional> getListUsers() {
        return listUsersInstitutional;
    }

    public boolean addUser(UserInstitutional user) {
        this.listUsersInstitutional.add(user);
        return true;
    }

    public boolean deleteUser(UserInstitutional user) {
        UserInstitutional userDelete = null;
        boolean removed = false;

        for( UserInstitutional u : listUsersInstitutional ){
            if( user.getId() == u.getId() ){
                userDelete = u;
                break;
            }
        }

        if( userDelete != null ){
            listUsersInstitutional.remove( userDelete );
            removed = true;

        }

        return removed;
    }

    public boolean editUser(UserInstitutional user) {
        UserInstitutional userEdit = null;
        boolean edited = false;

        for(UserInstitutional u : listUsersInstitutional){
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
            userEdit.setType(user.getType());
        }

        return edited;
    }
    public UserInstitutional getUserById(int idUser) {
        UserInstitutional user = null;

        for(UserInstitutional u : this.listUsersInstitutional){
            if( u.getId() == idUser ){
                user = u;
                break;
            }
        }

        return user;
    }
}
