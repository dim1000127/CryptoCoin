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
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.cryptocoin.R;
import com.example.cryptocoin.fragments.BookLearnFragment;
import com.example.cryptocoin.fragments.HomeFragment;
import com.example.cryptocoin.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class Main extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    //private Fragment selectedFragment;
    private Toolbar toolbar;
    private MenuItem menuItemSearch;
    private NavController navController;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main_screen);
        setSupportActionBar(toolbar);

        navController = Navigation.findNavController(this, R.id.fragment_container);
        bottomNav = findViewById(R.id.navigation_view);
        NavigationUI.setupWithNavController(bottomNav, navController);
        navController.addOnDestinationChangedListener(navControllerListener);
    }

    private NavController.OnDestinationChangedListener navControllerListener =
            new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                    if(navDestination.getId() == R.id.homeFragment) {
                        toolbar.setTitle("CryptoCoin");
                        if(menuItemSearch != null) {
                            menuItemSearch.setVisible(true);
                        }
                    }
                    else if (navDestination.getId() == R.id.listFragment){
                        toolbar.setTitle("Топ криптовалют");
                        if(menuItemSearch != null) {
                            menuItemSearch.setVisible(true);
                        }
                    }
                    else if(navDestination.getId() == R.id.bookLearnFragment) {
                        toolbar.setTitle("Обучение");
                        if(menuItemSearch != null) {
                            menuItemSearch.setVisible(false);
                        }
                    }
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
