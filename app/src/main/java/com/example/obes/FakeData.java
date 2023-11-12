package com.example.obes;

import com.example.obes.dao.UserCommonDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;

import java.util.ArrayList;

public class FakeData {
    public FakeData() {}

    public static void generateFakeUsers() {
        UserCommonDAO userCommonDAO = UserCommonDAO.getInstance();

        UserCommon user1 = new UserCommon(1, "Vitor Fernandes Rocha", "vitor@gmail.com", "12345");
        userCommonDAO.addUser(user1);

        UserCommon user2 = new UserCommon(2, "Gabriela Castro Barros", "gabriela@gmail.com", "12345");
        userCommonDAO.addUser(user2);

        UserCommon user3 = new UserCommon(3, "Bianca Cardoso Souza", "bianca@gmail.com", "12345");
        userCommonDAO.addUser(user3);

        // Para o user1
        user1.donateABook(new Book(1, "Dom Casmurro", "Um romance brasileiro clássico", "Ficção", true, R.drawable.cover_book1, "Machado de Assis", 0.0, "Novo"));
        user1.donateABook(new Book(2, "O Pequeno Príncipe", "Um conto para todas as idades", "Ficção", true, R.drawable.cover_book2, "Antoine de Saint-Exupéry", 0.0, "Usado"));

        // Para o user2
        user2.sellABook(new Book(4, "1984", "Distopia futurista", "Ficção Científica", true, R.drawable.cover_book2, "George Orwell", 19.99, "Usado"));
        user2.sellABook(new Book(5, "A Menina que Roubava Livros", "História durante a Segunda Guerra Mundial", "Drama", true, R.drawable.cover_book1, "Markus Zusak", 15.99, "Novo"));
        user2.sellABook(new Book(6, "Harry Potter e a Pedra Filosofal", "A primeira aventura de Harry Potter", "Fantasia", true, R.drawable.cover_book2, "J.K. Rowling", 24.99, "Usado"));

        // Para o user3
        user3.sellABook(new Book(7, "Senhor dos Anéis", "Uma jornada épica de fantasia", "Fantasia", true, R.drawable.cover_book1, "J.R.R. Tolkien", 29.99, "Novo"));
        user3.sellABook(new Book(8, "O Código Da Vinci", "Uma trama de mistério e conspiração", "Mistério", true, R.drawable.cover_book2, "Dan Brown", 12.99, "Usado"));

        user3.donateABook(new Book(9, "O Diário de Anne Frank", "O relato de uma jovem durante o Holocausto", "Biografia", true, R.drawable.cover_book1, "Anne Frank", 0.0, "Usado"));
        user3.donateABook(new Book(10, "Cem Anos de Solidão", "Um épico da literatura latino-americana", "Ficção", true, R.drawable.cover_book2, "Gabriel García Márquez", 0.0, "Novo"));
        user3.donateABook(new Book(11, "A Revolução dos Bichos", "Uma alegoria política", "Fábula", true, R.drawable.cover_book1, "George Orwell", 0.0, "Usado"));
    }
}