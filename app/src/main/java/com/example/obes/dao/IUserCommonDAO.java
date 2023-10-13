package com.example.obes.dao;

import com.example.obes.model.user.UserCommon;

import java.util.ArrayList;

public interface IUserCommonDAO {
    public boolean addUser(UserCommon user);
    public boolean deleteUser(UserCommon user);
    public boolean editUser(UserCommon user);
    public UserCommon getUserById(int idUser);
    public ArrayList<UserCommon> getListUsers();
}