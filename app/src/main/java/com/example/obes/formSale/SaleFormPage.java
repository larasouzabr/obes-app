package com.example.obes.formSale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.R;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.model.Book.Book;

import java.util.ArrayList;
import java.util.Collections;

public class SaleFormPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etPrice;
    private EditText etCategory;
    private EditText etAuthor;
    private EditText etCondition;
    private CheckBox cbTerms;
    private Button button_cancel;
    private Button button_next;
    private final BookDAO bookDonateDAO = BookDAO.getInstance();
    private final BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_form_page);

        this.startComponents();

        this.tvTitlePage.setText("Vender um livro");

        this.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheckedInputs = checkingInputs();

                if (isCheckedInputs) {
                    Book infBookForm = getBookForm();

                    Intent intent = new Intent(SaleFormPage.this, SalePreview.class);

                    intent.putExtra("book_id", infBookForm.getId());
                    intent.putExtra("book_title", infBookForm.getTitle());
                    intent.putExtra("book_description", infBookForm.getDescription());
                    intent.putExtra("book_category", infBookForm.getCategory());
                    intent.putExtra("book_available", infBookForm.getAvailable());
                    intent.putExtra("book_cover", infBookForm.getCoverResourceId());
                    intent.putExtra("book_author", infBookForm.getAuthor());
                    intent.putExtra("book_price", infBookForm.getPrice());
                    intent.putExtra("book_condition", infBookForm.getCondition());

                    startActivity(intent);
                }
            }
        });
    }
    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_next = findViewById(R.id.button_next);
        this.etTitle = findViewById(R.id.title);
        this.etPrice = findViewById(R.id.price);
        this.etDescription = findViewById(R.id.description);
        this.etCategory = findViewById(R.id.category);
        this.etAuthor = findViewById(R.id.author);
        this.etCondition = findViewById(R.id.condition);
        this.cbTerms = findViewById(R.id.terms);
    }

    private boolean checkingInputs() {
        Book bookForm = getBookForm();

        boolean terms = this.cbTerms.isChecked();

        if (bookForm.getTitle().isEmpty() || bookForm.getDescription().isEmpty() || bookForm.getCategory().isEmpty() || bookForm.getAuthor().isEmpty() || bookForm.getCondition().isEmpty()) {
            Toast.makeText(SaleFormPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (bookForm.getPrice() <= 0) {
            Toast.makeText(SaleFormPage.this, "Por favor, digite um preço válido para o livro", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!terms) {
            Toast.makeText(SaleFormPage.this, "Por favor, concorde com Termos de Venda e Doação", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Book getBookForm() {
        int id = countId();
        String title = this.etTitle.getText().toString();
        String description = this.etDescription.getText().toString();
        String category = this.etCategory.getText().toString();
        boolean available = true;
        int coverResourceId = 1;
        String author = this.etAuthor.getText().toString();
        double price = 0.0;
        String condition = this.etCondition.getText().toString();

        if (!this.etPrice.getText().toString().isEmpty()) {
            price = Double.parseDouble(this.etPrice.getText().toString());
        }

        Book book = new Book(id, title, description, category, available, coverResourceId, author, price, condition);

        return book;
    }

    public int countId() {
        ArrayList<Book> listAllBooks = new ArrayList<Book>();
        ArrayList<Book> listBooksSale = this.bookSaleDAO.getListBooks();
        ArrayList<Book> listBooksDonate = this.bookDonateDAO.getListBooks();

        listAllBooks.addAll(listBooksSale);
        listAllBooks.addAll(listBooksDonate);

        ArrayList<Integer> listIds = new ArrayList<Integer>();

        for (Book book : listAllBooks) {
            if (!listIds.contains(book.getId())) {
                listIds.add(book.getId());
            }
        }

        int id = 1;

        int amountBooks = listAllBooks.size();

        if (amountBooks > 0) {
            id = Collections.max(listIds) + 1;
        }

        return id;
    }
}