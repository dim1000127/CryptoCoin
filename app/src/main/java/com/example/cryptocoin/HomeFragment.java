package com.example.cryptocoin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    View rootView;
    ImageView imageViewCryptoTop1;
    ImageView imageViewCryptoTop2;
    ImageView imageViewCryptoTop3;
    TextView textViewSymbolCryptoOne;
    TextView textViewSymbolCryptoTwo;
    TextView textViewSymbolCryptoThree;
    TextView textViewChange24hCryptoOne;
    TextView textViewChange24hCryptoTwo;
    TextView textViewChange24hCryptoThree;
    TextView textViewPriceCryptoOne;
    TextView textViewPriceCryptoTwo;
    TextView textViewPriceCryptoThree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return  inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
