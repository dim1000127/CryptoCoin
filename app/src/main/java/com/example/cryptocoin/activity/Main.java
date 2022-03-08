package com.example.cryptocoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.R;
import com.example.cryptocoin.fragments.BookLearnFragment;
import com.example.cryptocoin.fragments.HomeFragment;
import com.example.cryptocoin.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

public class Main extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Fragment selectedFragment;
    private Toolbar toolbar;
    private MenuItem menuItemSearch;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_screen);
        setSupportActionBar(toolbar);

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
                            toolbar.setTitle("CryptoCoin");
                            menuItemSearch.setVisible(true);
                            break;
                        case R.id.navigation_list_top:
                            selectedFragment =  new ListFragment();
                            toolbar.setTitle("Топ криптовалют");
                            menuItemSearch.setVisible(true);
                            break;
                        case R.id.navigation_bool_learn:
                            selectedFragment =  new BookLearnFragment();
                            toolbar.setTitle("Обучение");
                            menuItemSearch.setVisible(false);
                            break;
                    }
                    if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_main, menu);
        menuItemSearch = menu.findItem(R.id.search_button);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_button:
                Intent intent = new Intent(Main.this, SearchCryptoValute.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
