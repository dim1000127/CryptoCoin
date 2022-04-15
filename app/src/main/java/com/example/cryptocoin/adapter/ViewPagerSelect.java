package com.example.cryptocoin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.cryptocoin.fragments.FragmentCryptoValuteSelect;
import com.example.cryptocoin.fragments.FragmentFiatSelect;

public class ViewPagerSelect extends FragmentStateAdapter {


    public ViewPagerSelect(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return  new FragmentFiatSelect();
            default:
                return new FragmentCryptoValuteSelect();

        }
    }



    @Override
    public int getItemCount() {
        return 2;
    }
}
