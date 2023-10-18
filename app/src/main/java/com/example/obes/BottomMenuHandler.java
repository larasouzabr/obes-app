package com.example.obes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class BottomMenuHandler {
    private Context context;

    public BottomMenuHandler(Context context) {
        this.context = context;
    }

    public void setupBottomMenu() {
        ImageView homeButton = ((Activity) context).findViewById(R.id.home);
        ImageView donateSaleButton = ((Activity) context).findViewById(R.id.donate_sale);
        ImageView cartButton = ((Activity) context).findViewById(R.id.cart);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePage.class);
                context.startActivity(intent);
            }
        });

        donateSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DonateSalePage.class);
                context.startActivity(intent);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}

