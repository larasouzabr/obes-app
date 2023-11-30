package com.example.obes.formSale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.obes.R;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.formDonate.DonatePreview;
import com.example.obes.model.Book.Book;
import com.example.obes.model.User.UserCommon;
import com.example.obes.perfil.PerfilUserCommon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

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
    private ImageView ivCover;
    private FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private DatabaseReference reference = rootNode.getReference("books_sale");
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    ProgressBar progressBar;
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
                Uri uri = Uri.parse(bookPreview.getCoverResourceId());

                final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));

                imageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                bookPreview.setCoverResourceId(uri.toString());
                                getUserLogged().sellABook(bookPreview);

                                DatabaseReference bookReference = reference.child(String.valueOf(bookPreview.getId()));

                                Map<String, Object> bookData = new HashMap<>();
                                bookData.put("id", bookPreview.getId());
                                bookData.put("title", bookPreview.getTitle());
                                bookData.put("description", bookPreview.getDescription());
                                bookData.put("category", bookPreview.getCategory());
                                bookData.put("available", bookPreview.isAvailable());
                                bookData.put("coverResourceId", bookPreview.getCoverResourceId());
                                bookData.put("author", bookPreview.getAuthor());
                                bookData.put("price", bookPreview.getPrice());
                                bookData.put("condition", bookPreview.getCondition());

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

                                progressBar.setVisibility(View.INVISIBLE);

                                showModal();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SalePreview.this, "Falha ao vender o livro", Toast.LENGTH_SHORT).show();
                    }
                });
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
        this.ivCover = findViewById(R.id.ivCover);
        this.progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private Book getInfBookForm() {
        Intent intentExtra = getIntent();

        int id = intentExtra.getIntExtra("book_id", 0);
        String title = intentExtra.getStringExtra("book_title");
        String description = intentExtra.getStringExtra("book_description");
        String category = intentExtra.getStringExtra("book_category");
        boolean available = intentExtra.getBooleanExtra("book_available", true);
        String cover = intentExtra.getStringExtra("book_cover");
        String author = intentExtra.getStringExtra("book_author");
        double price = intentExtra.getDoubleExtra("book_price", 0.0);
        String condition = intentExtra.getStringExtra("book_condition");

        Book book = new Book(id, title, description, category, available, cover, author, price, condition);

        return book;
    }

    private void setInfBookFormInPreview(Book book) {
        this.ivCover.setImageURI(Uri.parse(book.getCoverResourceId()));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SalePreview.this);
        LayoutInflater inflater = (LayoutInflater) SalePreview.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_modal, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final TextView tvTitleModal = dialogView.findViewById(R.id.title_modal);
        final TextView tvTextModal = dialogView.findViewById(R.id.text_modal);
        TextView tvButtonModal = dialogView.findViewById(R.id.button_modal);

        tvTitleModal.setText("Obrigado por vender no Obes");
        tvTextModal.setText("Você receberá um e-mail quando alguém solicitar a compra. Atente-se ao seu telefone e realize a entrega com responsabilidade!");

        tvButtonModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalePreview.this, PerfilUserCommon.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}