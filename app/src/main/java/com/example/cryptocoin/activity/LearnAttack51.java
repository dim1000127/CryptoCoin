package com.example.cryptocoin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.R;

public class LearnAttack51 extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklearn_attack51);

        ImageView imageViewBack = findViewById(R.id.imageView_back_learn_attach51);
        imageViewBack.setOnClickListener(view -> onBackPressed());
    }
}
