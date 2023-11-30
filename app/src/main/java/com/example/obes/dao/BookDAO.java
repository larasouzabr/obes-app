package com.example.obes.dao;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookDAO implements IBookDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("books_donate");
    private ArrayList<Book> listBooks;
    private static BookDAO instance;

    private BookDAO() {
        this.listBooks = new ArrayList<Book>();
    }

    public static BookDAO getInstance() {
        if(instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }

    public ArrayList<Book> getListBooks() {
        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Book> books = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    int id = dataSnapshot.child("id").getValue(int.class);
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String category = dataSnapshot.child("category").getValue(String.class);
                    boolean available = Boolean.TRUE.equals(dataSnapshot.child("available").getValue(boolean.class));
                    String coverResourceId = dataSnapshot.child("coverResourceId").getValue(String.class);
                    String author = dataSnapshot.child("author").getValue(String.class);
                    double price = dataSnapshot.child("price").getValue(double.class);
                    String condition = dataSnapshot.child("condition").getValue(String.class);

                    Book book = new Book(id, title, description, category, available, coverResourceId, author, price, condition);

                    books.add(book);
                }

                listBooks = books;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Erro ao recuperar usuários: " + error.getMessage());
            }
        });

        return this.listBooks;
    }

    public boolean addBook(Book book) {
        this.listBooks.add(book);

        return true;
    }

    public boolean deleteBook(Book book) {
        this.listBooks.remove(book);
        this.reference.child(String.valueOf(book.getId())).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Livro removido com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao remover o livro: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean editBook(Book book) {
        for(Book b : this.listBooks){
            if(book.getId() == b.getId()){
                b.setTitle(book.getTitle());
                b.setAuthor(book.getAuthor());
                b.setDescription(book.getDescription());
                b.setCategory(book.getCategory());
                b.setAvailable(book.getAvailable());
                b.setCoverResourceId(book.getCoverResourceId());
                b.setPrice(book.getPrice());
                b.setCondition(book.getCondition());
                break;
            }
        }

        DatabaseReference bookReference = this.reference.child(String.valueOf(book.getId()));

        Map<String, Object> bookData = new HashMap<>();
        bookData.put("id", book.getId());
        bookData.put("title", book.getTitle());
        bookData.put("description", book.getDescription());
        bookData.put("category", book.getCategory());
        bookData.put("available", book.isAvailable());
        bookData.put("coverResourceId", book.getCoverResourceId());
        bookData.put("author", book.getAuthor());
        bookData.put("price", book.getPrice());
        bookData.put("condition", book.getCondition());

        bookReference.setValue(bookData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Livro editado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao editar o livro: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }
    public Book getBookById(int idBook) {
        Book book = null;

        for(Book b : this.listBooks){
            if( b.getId() == idBook ){
                book = b;
                break;
            }
        }

        return book;
    }
}