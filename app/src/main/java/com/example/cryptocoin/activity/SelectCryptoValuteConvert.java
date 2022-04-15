package com.example.cryptocoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.ViewPagerSelect;
import com.example.cryptocoin.fragments.FragmentCryptoValuteSelect;
import com.example.cryptocoin.fragments.FragmentFiatSelect;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SelectCryptoValuteConvert extends AppCompatActivity implements FragmentFiatSelect.OnSelectFiatListener,
        FragmentCryptoValuteSelect.OnSelectCVListener {

    private SearchView searchView;

    private String symbolFiat = null;
    private String typeAsset = null;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cv_convert);
        Toolbar toolbar = findViewById(R.id.toolbar_select_convert_cv);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Выбрать актив");

        searchView = (SearchView) findViewById(R.id.searchview_select);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout_select);
        ViewPager2 viewPager2 = (ViewPager2) findViewById(R.id.viewpager_select);

        ViewPagerSelect viewPagerSelect = new ViewPagerSelect(this);
        viewPager2.setAdapter(viewPagerSelect);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(getString(R.string.cryptovaluts));
                } else if (position == 1) {
                    tab.setText(getString(R.string.fiat));
                }
            }
        }).attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setIconifiedByDefault(true);
                    searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primaryColor, null));
                } else if (position == 1) {
                    searchView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onSelectCV(String id){
        typeAsset = Const.CRYPTOVALUTE;
        Intent data = new Intent();
        data.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, id);
        data.putExtra(Const.TYPE_ASSET, typeAsset);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onSelectFiatUsd(){
        symbolFiat = Const.USD_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(Const.TYPE_ASSET, typeAsset);
        data.putExtra(Const.SYMBOL_MESSAGE, symbolFiat);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onSelectFiatRub(){
        symbolFiat = Const.RUB_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(Const.TYPE_ASSET, typeAsset);
        data.putExtra(Const.SYMBOL_MESSAGE, symbolFiat);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
