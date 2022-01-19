package com.example.cryptocoin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Fragment selectedFragment;
    ActionBar actionBar;

    private static final String apiKey = "908e2080-ad8d-4d43-bd0a-e65b8587d172";
    public static final String BASE_URL = "https://pro-api.coinmarketcap.com";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        actionBar = getSupportActionBar();

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
                            //APIGetPriceCall(0);
                            //необходимо будет сделать свой ActionBar
                            actionBar.setTitle("CryptoCoin");
                            break;
                        case R.id.navigation_list_top:
                            selectedFragment =  new ListFragment();
                            //APIGetPriceCall(1);
                            actionBar.setTitle("Топ криптовалют");
                            break;
                        case R.id.navigation_bool_learn:
                            selectedFragment =  new BookLearnFragment();
                            actionBar.setTitle("Обучение");
                            break;
                    }
                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };
}
