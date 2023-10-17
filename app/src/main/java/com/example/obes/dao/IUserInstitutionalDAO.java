package com.example.obes.dao;

import com.example.obes.model.user.UserInstitutional;

import java.util.ArrayList;

public interface IUserInstitutionalDAO {
    public boolean addUser(UserInstitutional user);
    public boolean deleteUser(UserInstitutional user);
    public boolean editUser(UserInstitutional user);
    public UserInstitutional getUserById(int idUser);
    public ArrayList<UserInstitutional> getListUsers();
}
