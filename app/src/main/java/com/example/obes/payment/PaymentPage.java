package com.example.obes.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.obes.CartPage;
import com.example.obes.R;
import com.example.obes.dao.CartToItemDAO;
import com.example.obes.dao.CartToUserDAO;
import com.example.obes.dao.ItemCartDAO;
import com.example.obes.dao.LoginSessionManager;
import com.example.obes.dao.Review.ReviewDAO;
import com.example.obes.dao.Review.UserHasReviewDAO;
import com.example.obes.model.Book.Book;
import com.example.obes.model.Cart.Cart;
import com.example.obes.model.Cart.ItemCart;
import com.example.obes.model.Request.ItemRequest;
import com.example.obes.model.Request.Request;
import com.example.obes.model.User.User;
import com.example.obes.perfil.PerfilUserCommon;
import com.example.obes.perfil.PerfilUserInstitutional;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PaymentPage extends AppCompatActivity {
    private TextView tvTitlePage;
    private ImageView backArrow;
    private Button buttonCancel;
    private Button buttonBuy;
    private ViewPager2 vpTabPayments;
    private TabLayout tlPayments;
    private MyViewPageAdapterPayments myViewPageAdapterPayments;
    private LoginSessionManager loginSessionManager;
    private User userLogged;
    private int PAYMENT_SELECTED = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        this.startComponents();

        this.tvTitlePage.setText("Pagamento");

        userLogged = this.getUserLogged();

        this.showTabLayoutBooks();

        Request newRequest = getIntent().getExtras().getParcelable("new_request");
        ArrayList<ItemRequest> newItems = getIntent().getParcelableArrayListExtra("new_items");
        ArrayList<Book> booksSelected = getIntent().getParcelableArrayListExtra("books_selected");

        for (int index = 0; index < newItems.size(); index++) {
            newItems.get(index).setItem(booksSelected.get(index));
        }

        this.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModalConfirmBuy(newRequest, newItems);
            }
        });
    }

    private void startComponents() {
        this.tvTitlePage = findViewById(R.id.title_page);
        this.backArrow = findViewById(R.id.back_arrow);
        this.buttonCancel = findViewById(R.id.button_cancel);
        this.buttonBuy = findViewById(R.id.button_buy);
        this.vpTabPayments = findViewById(R.id.layout_tab_payments);
        this.tlPayments = findViewById(R.id.tab_payments);
        this.myViewPageAdapterPayments = new MyViewPageAdapterPayments(this);
        this.loginSessionManager = LoginSessionManager.getInstance();
    }

    private void showTabLayoutBooks() {
        // TabLayout Payments
        vpTabPayments.setAdapter(this.myViewPageAdapterPayments);

        vpTabPayments.setUserInputEnabled(false);

        tlPayments.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTabPayments.setCurrentItem(tab.getPosition());
                PAYMENT_SELECTED = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpTabPayments.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tlPayments.getTabAt(position).select();
            }
        });
    }

    private User getUserLogged() {
        User userLogged;

        if (loginSessionManager.getCurrentUserCommon() == null) {
            userLogged = loginSessionManager.getCurrentUserInstitutional();
        } else {
            userLogged = loginSessionManager.getCurrentUserCommon();
        }

        return userLogged;
    }

    public void cleanCart() {
        CartToUserDAO cartToUserDAO = CartToUserDAO.getInstance();
        Cart cartUserLogged = cartToUserDAO.getCartByIdUser(userLogged.getId());

        CartToItemDAO cartToItemDAO = CartToItemDAO.getInstance();
        ArrayList<ItemCart> itemsUserLogged = cartToItemDAO.getItemsByIdCart(cartUserLogged.getId());

        ItemCartDAO itemCartDAO = ItemCartDAO.getInstance();
        for (ItemCart item : itemsUserLogged) {
            itemCartDAO.deleteItemCart(item);
            cartToItemDAO.deleteCartItem(cartUserLogged.getId(), item.getId());
        }
    }

    public boolean userLoggedIsCommon() {
        if (loginSessionManager.getCurrentUserCommon() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void showModalProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentPage.this);
        LayoutInflater inflater = (LayoutInflater) PaymentPage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_modal, null);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final TextView tvTitleModal = dialogView.findViewById(R.id.title_modal);
        final TextView tvTextModal = dialogView.findViewById(R.id.text_modal);
        TextView tvButtonModal = dialogView.findViewById(R.id.button_modal);

        tvTitleModal.setText("Pedido finalizado com sucesso!");
        tvTextModal.setText("Você receberá um e-mail quando alguém confirmar/cancelar o pedido.");

        tvButtonModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (userLoggedIsCommon()) {
                    intent = new Intent(PaymentPage.this, PerfilUserCommon.class);
                } else {
                    intent = new Intent(PaymentPage.this, PerfilUserInstitutional.class);
                }

                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void buy(Request newRequest,  ArrayList<ItemRequest> newItems) {
        userLogged.saleRequest(newRequest, newItems);

        cleanCart();

        showModalProfile();
    }

    public void showModalConfirmBuy(Request newRequest,  ArrayList<ItemRequest> newItems) {
        final Dialog modal = new Dialog(PaymentPage.this);

        modal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        modal.setCancelable(true);
        modal.setContentView(R.layout.modal_delete_book);

        Button buttonCancel = modal.findViewById(R.id.button_cancel);
        Button buttonDelete = modal.findViewById(R.id.button_delete);
        TextView tvDescription = modal.findViewById(R.id.description);

        tvDescription.setText("Tem certeza que deseja finalizar a compra?");
        buttonDelete.setText("Comprar");

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy(newRequest, newItems);

                modal.dismiss();
            }
        });

        modal.show();
    }
}