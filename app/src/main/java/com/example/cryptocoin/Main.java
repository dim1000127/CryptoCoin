package com.example.cryptocoin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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
    public static CryptoValute cryptoValute = null;

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
        APICall();
        //selectedFragment = new HomeFragment(cryptoValute);
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){

                        case R.id.navigation_home:
                            //selectedFragment =  new HomeFragment(cryptoValute);
                            selectedFragment =  new HomeFragment();
                            //необходимо будет сделать свой ActionBar
                            actionBar.setTitle("CryptoCoin");
                            break;
                        case R.id.navigation_list_top:
                            selectedFragment =  new ListFragment();
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

    public void APICall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestsAPI requestsAPI = retrofit.create(RequestsAPI.class);

        Call<CryptoValute> dataCryptoValute = requestsAPI.getDataCryptoValute(1,3,"USD");

        dataCryptoValute.enqueue(new Callback<CryptoValute>() {
            @Override
            public void onResponse(Call<CryptoValute> call, Response<CryptoValute> response) {
                if(response.isSuccessful()){
                    CryptoValute dataCryptoValute = null;
                    dataCryptoValute = response.body();
                    cryptoValute = dataCryptoValute;
                    //actionBar.setTitle(String.valueOf(dataCryptoValute.getData().get(1).getQuote().getUsdDataCoin().getPrice()));
                    Log.d("List ", String.valueOf(response.body()));
                }
                else{
                    Log.d("Response code ", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<CryptoValute> call, Throwable t) {
                Log.d("Failure", t.toString());
                actionBar.setTitle("BTC");
            }
        });
    }
}
