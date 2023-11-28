package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.User.UserCommon;
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

public class UserCommonDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("users");
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
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserCommon> users = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    String contact = dataSnapshot.child("contact").getValue(String.class);
                    String cpf = dataSnapshot.child("cpf").getValue(String.class);
                    String dateOfBirth = dataSnapshot.child("dateOfBirth").getValue(String.class);
                    String about = dataSnapshot.child("about").getValue(String.class);

                    UserCommon user = new UserCommon(id, name, email, password);
                    user.setContact(contact);
                    user.setCpf(cpf);
                    user.setDateOfBirth(dateOfBirth);
                    user.setAbout(about);

                    users.add(user);
                }

                listUsersCommon = users;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar usuários: " + error.getMessage());
            }
        });

        return this.listUsersCommon;
    }

    public boolean addUser(UserCommon user) {
        this.listUsersCommon.add(user);

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
    public boolean deleteUser(UserCommon user) {
        this.listUsersCommon.remove(user);

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

    public boolean editUser(UserCommon user) {
        for (UserCommon u : this.listUsersCommon) {
            if (u.getId() == user.getId()) {
                u.setName(user.getName());
                u.setAbout(user.getAbout());
                u.setContact(user.getContact());
                u.setEmail(user.getEmail());
                u.setPassword(user.getPassword());
                u.setCpf(user.getCpf());
                u.setDateOfBirth(user.getDateOfBirth());
            }
        }

        DatabaseReference userReference = this.reference.child(String.valueOf(user.getId()));

        LoginSessionManager.getInstance().loginUserCommon(user);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("name", user.getName());
        userData.put("about", user.getAbout());
        userData.put("contact", user.getContact());
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());
        userData.put("cpf", user.getCpf());
        userData.put("dateOfBirth", user.getDateOfBirth());

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
