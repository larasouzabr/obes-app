package com.example.obes.formDonate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.obes.R;
import com.example.obes.dao.BookDAO;
import com.example.obes.dao.BookSaleDAO;
import com.example.obes.model.Book.Book;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class DonateFormPage extends AppCompatActivity {
    private EditText etTitle;
    private EditText etDescription;
    private EditText etCategory;
    private EditText etAuthor;
    private EditText etCondition;
    private ImageView ivCover;
    private Button buttonImage;
    private ImageButton buttonCamera;
    private CheckBox cbTerms;
    private Button button_cancel;
    private Button button_next;
    private BookDAO bookDonateDAO = BookDAO.getInstance();
    private BookSaleDAO bookSaleDAO = BookSaleDAO.getInstance();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_form_page);

        this.startComponents();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            ivCover.setImageURI(imageUri);
                        } else {
                            Toast.makeText(DonateFormPage.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        this.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        this.buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheckedInputs = checkingInputs();

                if (isCheckedInputs) {
                    Book infBookForm = getBookForm();

                    Intent intent = new Intent(DonateFormPage.this, DonatePreview.class);

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
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startComponents() {
        this.button_cancel = findViewById(R.id.button_cancel);
        this.button_next = findViewById(R.id.button_next);
        this.etTitle = findViewById(R.id.title);
        this.etDescription = findViewById(R.id.description);
        this.etCategory = findViewById(R.id.category);
        this.etAuthor = findViewById(R.id.author);
        this.etCondition = findViewById(R.id.condition);
        this.ivCover = findViewById(R.id.iv_cover);
        this.buttonImage = findViewById(R.id.button_image);
        this.buttonCamera = findViewById(R.id.button_camera);
        this.cbTerms = findViewById(R.id.terms);
    }

    private boolean checkingInputs() {
        Book bookForm = getBookForm();

        boolean terms = this.cbTerms.isChecked();

        if (bookForm.getTitle().isEmpty() || bookForm.getDescription().isEmpty() || bookForm.getCategory().isEmpty() || bookForm.getAuthor().isEmpty() || bookForm.getCondition().isEmpty()) {
            Toast.makeText(DonateFormPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!terms) {
            Toast.makeText(DonateFormPage.this, "Por favor, concorde com Termos de Venda e Doação", Toast.LENGTH_SHORT).show();
            return false;
        } else if (this.imageUri == null) {
            Toast.makeText(DonateFormPage.this, "Por favor, adicione uma imagem", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

    private Book getBookForm() {
        int id = countId();
        String title = this.etTitle.getText().toString();
        String description = this.etDescription.getText().toString();
        String category = this.etCategory.getText().toString();
        boolean available = true;

        String coverResourceId = null;
        if (this.imageUri != null) {
            coverResourceId = this.imageUri.toString();
        }

        String author = this.etAuthor.getText().toString();
        double price = 0.00;
        String condition = this.etCondition.getText().toString();

        Book book = new Book(id, title, description, category, available, coverResourceId, author, price, condition);

        return book;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(this, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri = Uri.fromFile(new File(currentPhotoPath));
            ivCover.setImageURI(imageUri);
        }
    }
}