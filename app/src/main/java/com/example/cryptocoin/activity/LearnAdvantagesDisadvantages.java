package com.example.cryptocoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.R;

public class LearnAdvantagesDisadvantages extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklearn_advantag_disadvantag);

        ImageView imageViewBack = (ImageView) findViewById(R.id.imageView_back_learn_advantag_disadvantag);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}