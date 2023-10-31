package com.example.obes.perfil;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.obes.perfil.fragments.BooksDonate;
import com.example.obes.perfil.fragments.BooksRequest;
import com.example.obes.perfil.fragments.BooksSale;

public class MyViewPageAdapterBooks extends FragmentStateAdapter {
    public MyViewPageAdapterBooks(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BooksRequest();
            case 1:
                return new BooksSale();
            case 2:
                return new BooksDonate();
            default:
                return new BooksRequest();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}