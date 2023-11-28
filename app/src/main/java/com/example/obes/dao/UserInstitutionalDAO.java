package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.User.UserCommon;
import com.example.obes.model.User.UserInstitutional;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInstitutionalDAO implements IUserInstitutionalDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("users_institutional");
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
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserInstitutional> users = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    String type = dataSnapshot.child("type").getValue(String.class);

                    String contact = dataSnapshot.child("contact").getValue(String.class);
                    String about = dataSnapshot.child("about").getValue(String.class);

                    UserInstitutional user = new UserInstitutional(id, name, email, password, type);
                    user.setContact(contact);
                    user.setAbout(about);

                    users.add(user);
                }

                listUsersInstitutional = users;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar usuários: " + error.getMessage());
            }
        });

        return this.listUsersInstitutional;
    }

    public boolean addUser(UserInstitutional user) {
        this.reference.child(String.valueOf(user.getId())).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG","Usuário cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteUser(UserInstitutional user) {
        this.reference.child(String.valueOf(user.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Usuário removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean editUser(UserInstitutional user) {
        DatabaseReference userReference = this.reference.child(String.valueOf(user.getId()));

        LoginSessionManager.getInstance().loginUserInstitutional(user);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());
        userData.put("type", user.getType());
        userData.put("about", user.getAbout());
        userData.put("contact", user.getContact());

        userReference.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Usuário editado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o usuário: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }
    public UserInstitutional getUserById(int idUser) {
        UserInstitutional user = null;

        for(UserInstitutional u : this.getListUsers()){
            if( u.getId() == idUser ){
                user = u;
                break;
            }
        }

        return user;
    }
}