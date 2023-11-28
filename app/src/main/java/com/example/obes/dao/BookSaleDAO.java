package com.example.obes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.obes.model.Book.Book;
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

public class BookSaleDAO implements IBookDAO {
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("books_sale");
    private ArrayList<Book> listBooks;
    private static BookSaleDAO instance;

    private BookSaleDAO() {
        this.listBooks = new ArrayList<Book>();
    }

    public static BookSaleDAO getInstance() {
        if(instance == null) {
            instance = new BookSaleDAO();
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
                    int coverResourceId = dataSnapshot.child("coverResourceId").getValue(int.class);
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
                    Log.d("TAG", "Livro cadastrado com sucesso");
                } else {
                    Log.e("TAG", "Ocorreu um erro ao cadastrar o livro: " + task.getException().getMessage());
                }
            }
        });

        return true;
    }

    public boolean deleteBook(Book book) {
        Book bookDelete = null;
        boolean removed = false;

        for( Book b : listBooks ){
            if( book.getId() == b.getId() ){
                bookDelete = b;
                break;
            }
        }

        if( bookDelete != null ){
            listBooks.remove( bookDelete );
            removed = true;

        }

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

        return removed;
    }

    public boolean editBook(Book book) {
        Book bookEdit = null;
        boolean edited = false;

        for(Book b : listBooks){
            if(book.getId() == b.getId()){
                bookEdit = b;
                edited = true;
                break;
            }
        }

        if(edited){
            bookEdit.setTitle(book.getTitle());
            bookEdit.setAuthor(book.getAuthor());
            bookEdit.setDescription(book.getDescription());
            bookEdit.setCategory(book.getCategory());
            bookEdit.setAvailable(book.getAvailable());
            bookEdit.setCoverResourceId(book.getCoverResourceId());
            bookEdit.setPrice(book.getPrice());
            bookEdit.setCondition(book.getCondition());
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

        return edited;
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