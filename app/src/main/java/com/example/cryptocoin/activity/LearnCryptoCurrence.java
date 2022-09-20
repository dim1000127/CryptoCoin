package com.example.cryptocoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.R;

public class LearnCryptoCurrence extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklearn_cryptocurrence);

        ImageView imageViewBack = findViewById(R.id.imageView_back_learn_cryptocurrence);
        imageViewBack.setOnClickListener(view -> onBackPressed());
    }
}
