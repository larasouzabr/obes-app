package com.example.obes.perfil;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.obes.perfil.fragments.ReviewGranted;
import com.example.obes.perfil.fragments.ReviewReceived;

public class MyViewPageAdapterReview extends FragmentStateAdapter {
    public MyViewPageAdapterReview(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ReviewReceived();
            case 1:
                return new ReviewGranted();
            default:
                return new ReviewReceived();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}