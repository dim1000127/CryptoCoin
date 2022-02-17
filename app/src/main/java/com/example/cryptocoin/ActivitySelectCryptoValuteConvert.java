package com.example.cryptocoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.adapter.AdapterSelectCryptoValute;
import com.example.cryptocoin.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.metadatapojo.Metadata;
import com.google.android.material.snackbar.Snackbar;

public class ActivitySelectCryptoValuteConvert extends AppCompatActivity {

    private ListView listViewSelectCryptoValute;
    private AdapterSelectCryptoValute adapterSelectCryptoValute;
    private LinearLayout layoutFiatUsdSelect;
    private LinearLayout layoutFiatRubSelect;

    private CryptoValute dataCryptoValute = null;
    private Metadata metadata = null;

    private int idCryptoValute = 0;
    private double priceCryptoValute = 0;
    private String symbolViat = null;
    private String symbolCryptoValute = null;
    private String typeAsset = null;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cv_convert);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Выбрать актив");

        Bundle extras = getIntent().getExtras();
        dataCryptoValute = (CryptoValute) extras.getSerializable(Const.CRYPTOVALUTE_INTENT);
        metadata = (Metadata) extras.getSerializable(Const.METADATA_INTENT);

        adapterSelectCryptoValute = new AdapterSelectCryptoValute(dataCryptoValute, metadata);

        layoutFiatUsdSelect = (LinearLayout) findViewById(R.id.layout_fiat_usd_select);
        layoutFiatRubSelect = (LinearLayout) findViewById(R.id.layout_fiat_rub_select);
        listViewSelectCryptoValute = (ListView) findViewById(R.id.listview_select_cryptovalute);
        listViewSelectCryptoValute.setAdapter(adapterSelectCryptoValute);

        listViewSelectCryptoValute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                priceCryptoValute = dataCryptoValute.getData().get(i).getQuote().getUsdDataCoin().getPrice();
                symbolCryptoValute = dataCryptoValute.getData().get(i).getSymbol();
                idCryptoValute = dataCryptoValute.getData().get(i).getId();
                typeAsset = Const.CRYPTOVALUTE;

                Intent data = new Intent();
                data.putExtra(ActivityConvertCryptoValute.PRICE_MESSAGE, priceCryptoValute);
                data.putExtra(ActivityConvertCryptoValute.SYMBOL_MESSAGE, symbolCryptoValute);
                data.putExtra(ActivityConvertCryptoValute.ID_MESSAGE, idCryptoValute);
                data.putExtra(ActivityConvertCryptoValute.TYPE_ASSET, typeAsset);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        layoutFiatUsdSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecetFiatUsd();
            }
        });

        layoutFiatRubSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(layoutFiatRubSelect, R.string.soon, Snackbar.LENGTH_SHORT).show();
                //selectFiatRub();
            }
        });
    }

    private void selecetFiatUsd(){
        symbolViat = Const.USD_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(ActivityConvertCryptoValute.TYPE_ASSET, typeAsset);
        data.putExtra(ActivityConvertCryptoValute.SYMBOL_MESSAGE, symbolViat);
        setResult(RESULT_OK, data);
        finish();
    }

    private void selectFiatRub(){
        symbolViat = Const.RUB_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(ActivityConvertCryptoValute.TYPE_ASSET, typeAsset);
        data.putExtra(ActivityConvertCryptoValute.SYMBOL_MESSAGE, symbolViat);
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
