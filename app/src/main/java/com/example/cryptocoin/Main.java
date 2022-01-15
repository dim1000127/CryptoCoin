package com.example.cryptocoin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Fragment selectedFragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        bottomNav = findViewById(R.id.navigation_view);
        bottomNav.setOnItemSelectedListener(navListener);
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.navigation_home:
                        selectedFragment =  new HomeFragment();
                        //необходимо будет сделать свой ActionBar
                        getSupportActionBar().setTitle("CryptoCoin");
                        break;
                    case R.id.navigation_list_top:
                        selectedFragment =  new ListFragment();
                        getSupportActionBar().setTitle("Топ криптовалют");
                        break;
                    case R.id.navigation_bool_learn:
                        selectedFragment =  new BookLearnFragment();
                        getSupportActionBar().setTitle("Обучение");
                        break;
                }
                if(selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
    };
}
