package com.example.cryptocoin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.R;
import com.google.android.material.snackbar.Snackbar;

public class FragmentFiatSelect extends Fragment {

    private LinearLayout layoutFiatUsdSelect;
    private LinearLayout layoutFiatRubSelect;

    public interface OnSelectFiatListener{
        void onSelectFiatUsd();
        void onSelectFiatRub();
    }

    private OnSelectFiatListener onSelectFiatListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSelectFiatListener = (OnSelectFiatListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement onSelectFiatListener");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_fiat_select, container, false);
        layoutFiatUsdSelect = (LinearLayout) view.findViewById(R.id.layout_fiat_usd_select);
        layoutFiatRubSelect = (LinearLayout) view.findViewById(R.id.layout_fiat_rub_select);

        layoutFiatUsdSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectFiatListener.onSelectFiatUsd();
            }
        });

        layoutFiatRubSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(layoutFiatRubSelect, R.string.soon, Snackbar.LENGTH_SHORT).show();
                onSelectFiatListener.onSelectFiatRub();
            }
        });
        return   view;
    }
}
