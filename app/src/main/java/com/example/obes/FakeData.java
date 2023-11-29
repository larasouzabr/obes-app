package com.example.obes;

import com.example.obes.dao.AddressDAO;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.CartDAO;
import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.ItemCartDAO;
import com.example.obes.dao.Payment.PaymentDAO;
import com.example.obes.dao.Payment.PaymentToItemDAO;
import com.example.obes.dao.Payment.PaymentToUserDAO;
import com.example.obes.dao.Request.ItemRequestDAO;
import com.example.obes.dao.Request.OrderDAO;
import com.example.obes.dao.Request.OrderItemDAO;
import com.example.obes.dao.Request.RequestDAO;
import com.example.obes.dao.Request.RequestToItemDAO;
import com.example.obes.dao.Request.RequestToUserDAO;
import com.example.obes.dao.Review.ReviewDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.dao.UserCommonDAO;
import com.example.obes.dao.UserInstitutionalDAO;
import com.example.obes.dao.UserRegisteredBookDonateDAO;
import com.example.obes.dao.UserRegisteredBookSaleDAO;
import com.example.obes.dao.Wishlist.ItemWishlistDAO;
import com.example.obes.dao.Wishlist.WishlistDAO;
import com.example.obes.dao.Wishlist.WishlistToItemDAO;
import com.example.obes.dao.Wishlist.WishlistToUserDAO;
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

    public static void generateData() {
        UserCommonDAO.getInstance().getListUsers();

        UserInstitutionalDAO.getInstance().getListUsers();

        BookDAO.getInstance().getListBooks();

        UserRegisteredBookDonateDAO.getInstance().getListUserBook();

        BookSaleDAO.getInstance().getListBooks();

        UserRegisteredBookSaleDAO.getInstance().getListUserBook();

        CartDAO.getInstance().getListCarts();

        ItemCartDAO.getInstance().getListItensCart();

        CartToItemDAO.getInstance().getListCartsItems();

        CartToUserDAO.getInstance().getListCartUser();

        AddressDAO.getInstance().getListAddress();

        WishlistDAO.getInstance().getWishlists();

        ItemWishlistDAO.getInstance().getListItems();

        WishlistToItemDAO.getInstance().getListWishItem();

        WishlistToUserDAO.getInstance().getListWishUser();

        ReviewDAO.getInstance().getListReviews();

        UserHasReviewDAO.getInstance().getListUserHasReview();

        PaymentDAO.getInstance().getListPayment();

        PaymentToUserDAO.getInstance().getListPaymentToUser();

        PaymentToItemDAO.getInstance().getListPaymentToItem();

        RequestDAO.getInstance().getListRequests();

        ItemRequestDAO.getInstance().getListItemRequests();

        RequestToItemDAO.getInstance().getListRequestItems();

        RequestToUserDAO.getInstance().getListRequestUser();

        OrderDAO.getInstance().getListRequestUser();

        OrderItemDAO.getInstance().getListItemsUser();
    }
}