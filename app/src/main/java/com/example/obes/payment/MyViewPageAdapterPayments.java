package com.example.obes.payment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.obes.payment.fragments.Boleto;
import com.example.obes.payment.fragments.Card;
import com.example.obes.payment.fragments.Pix;

public class MyViewPageAdapterPayments extends FragmentStateAdapter {
    public MyViewPageAdapterPayments(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Card();
            case 1:
                return new Boleto();
            case 2:
                return new Pix();
            default:
                return new Card();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}