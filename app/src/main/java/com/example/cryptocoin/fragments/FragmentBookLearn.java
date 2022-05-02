package com.example.cryptocoin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.R;
import com.example.cryptocoin.activity.LearnAdvantagesDisadvantages;
import com.example.cryptocoin.activity.LearnAttack51;
import com.example.cryptocoin.activity.LearnBlockchain;
import com.example.cryptocoin.activity.LearnConsensus;
import com.example.cryptocoin.activity.LearnCryptoCurrence;
import com.example.cryptocoin.activity.LearnFees;

public class FragmentBookLearn extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_booklearn, container, false);
        LinearLayout layoutLearnCryptocurrence = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_cryptocurrence);
        LinearLayout layoutLearnBlockchain = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_blockchain);
        LinearLayout layoutLearnConsensus = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_consensus);
        LinearLayout layoutLearnFees = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_fees);
        LinearLayout layoutLearnAdvantagDisadvantag = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_advantag_disadvantag);
        LinearLayout layoutLearnAttack51 = (LinearLayout) rootView.findViewById(R.id.layout_open_learn_attack51);

        layoutLearnCryptocurrence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnCryptoCurrence.class);
                startActivity(intent);
            }
        });

        layoutLearnBlockchain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnBlockchain.class);
                startActivity(intent);
            }
        });

        layoutLearnConsensus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnConsensus.class);
                startActivity(intent);
            }
        });

        layoutLearnFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnFees.class);
                startActivity(intent);
            }
        });

        layoutLearnAdvantagDisadvantag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnAdvantagesDisadvantages.class);
                startActivity(intent);
            }
        });

        layoutLearnAttack51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LearnAttack51.class);
                startActivity(intent);
            }
        });
        return  rootView;
    }
}
