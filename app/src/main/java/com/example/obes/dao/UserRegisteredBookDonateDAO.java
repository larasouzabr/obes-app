package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
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

public class UserRegisteredBookDonateDAO implements IUserRegisteredBookDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("user_registered_book_donate");
    private ArrayList<UserBook> listUserBook;
    private static UserRegisteredBookDonateDAO instance;

    private UserRegisteredBookDonateDAO() {
        this.listUserBook = new ArrayList<UserBook>();
    }

    public static UserRegisteredBookDonateDAO getInstance() {
        if(instance == null) {
            instance = new UserRegisteredBookDonateDAO();
        }
        return instance;
    }

    public ArrayList<UserBook> getListUserBook() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserBook> userBooks = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int idUser = dataSnapshot.child("idUser").getValue(int.class);
                    int idBook = dataSnapshot.child("idBook").getValue(int.class);

                    UserBook userBook = new UserBook(idUser, idBook);

                    userBooks.add(userBook);
                }

                listUserBook = userBooks;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar relação de usuário e livro: " + error.getMessage());
            }
        });

        return this.listUserBook;
    }

    public boolean addUserBook(int id_user, int id_book) {
        UserBook userBook = new UserBook(id_user, id_book);

        this.listUserBook.add(userBook);

        DatabaseReference bookReference = this.reference.child(userBook.getId_user() + "_" + userBook.getId_book());

        Map<String, Object> userBookData = new HashMap<>();
        userBookData.put("idUser", userBook.getId_user());
        userBookData.put("idBook", userBook.getId_book());

        bookReference.setValue(userBookData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de usuário e livro cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar a relação de usuário e livro: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteUserBook(int id_user, int id_book) {
        for(UserBook userBook : this.listUserBook) {
            if(userBook.getId_user() == id_user && userBook.getId_book() == id_book) {
                this.listUserBook.remove(userBook);
                break;
            }
        }

        String bookUser = id_user + "_" + id_book;
        this.reference.child(bookUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Relação de usuário e livro removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o relação de usuário e livro: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public ArrayList<Book> getListBookByUser(int id_user) {
        BookDAO bookDAO = BookDAO.getInstance();

        ArrayList<Book> listBooks = new ArrayList<Book>();

        for (UserBook userBook : this.listUserBook) {
            if (userBook.getId_user() == id_user) {
                listBooks.add(bookDAO.getBookById(userBook.getId_book()));
            }
        }

        return listBooks;
    }

    public UserCommon getUserByIdBook(int idBook) {
        UserCommon user = null;

        for (UserBook ub : this.listUserBook) {
            if (ub.getId_book() == idBook) {
                user = UserCommonDAO.getInstance().getUserById(ub.getId_user());
                break;
            }
        }

        return user;
    }
}