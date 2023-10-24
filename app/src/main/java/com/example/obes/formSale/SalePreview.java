package com.example.obes.formSale;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.obes.R;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;

public class SalePreview extends AppCompatActivity {
    private TextView tvTitlePage;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPrice;
    private TextView tvDescription;
    private TextView tvUserName;
    private Button button_back;
    private Button button_donate;
    private LoginSessionManager loginSessionManager = LoginSessionManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_preview);

        this.startComponents();

        this.tvTitlePage.setText("Preview");

        Book bookPreview = this.getInfBookForm();

        this.setInfBookFormInPreview(bookPreview);

        this.tvUserName.setText(this.getUserLogged().getName());

        this.button_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserLogged().sellABook(bookPreview);

                showModal();
            }
        });
        this.button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_back = findViewById(R.id.button_back);
        this.button_donate = findViewById(R.id.button_donate);
        this.tvTitle = findViewById(R.id.title);
        this.tvAuthor = findViewById(R.id.author);
        this.tvPrice = findViewById(R.id.price);
        this.tvDescription = findViewById(R.id.description);
        this.tvUserName = findViewById(R.id.user_name);
    }

    private Book getInfBookForm() {
        Intent intentExtra = getIntent();

        int id = intentExtra.getIntExtra("book_id", 0);
        String title = intentExtra.getStringExtra("book_title");
        String description = intentExtra.getStringExtra("book_description");
        String category = intentExtra.getStringExtra("book_category");
        boolean available = intentExtra.getBooleanExtra("book_available", true);
        int cover = intentExtra.getIntExtra("book_cover", 0);
        String author = intentExtra.getStringExtra("book_author");
        double price = intentExtra.getDoubleExtra("book_price", 0.0);
        String condition = intentExtra.getStringExtra("book_condition");

        Book book = new Book(id, title, description, category, available, cover, author, price, condition);

        return book;
    }

    private void setInfBookFormInPreview(Book book) {
        this.tvTitle.setText(book.getTitle());
        this.tvAuthor.setText(book.getAuthor());
        this.tvPrice.setText(Double.toString(book.getPrice()));
        this.tvDescription.setText(book.getDescription());
    }

    private UserCommon getUserLogged() {
        UserCommon user = loginSessionManager.getCurrentUserCommon();
        return user;
    }

    private void showModal() {
        final Dialog modal = new Dialog(SalePreview.this);

        modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modal.setCancelable(true);
        modal.setContentView(R.layout.custom_modal);

        final TextView tvTitleModal = modal.findViewById(R.id.title_modal);
        final TextView tvTextModal = modal.findViewById(R.id.text_modal);
        TextView tvButtonModal = modal.findViewById(R.id.button_modal);

        tvTitleModal.setText("Obrigado por vender no Obes");
        tvTextModal.setText("Você receberá um e-mail quando alguém solicitar a compra. Atente-se ao seu telefone e realize a entrega com responsabilidade!");

        tvButtonModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Vai para a página de perfil do usuário");
            }
        });

        modal.show();
    }
}