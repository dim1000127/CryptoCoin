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

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import java.io.Serializable;

public class ActivitySelectCryptoValuteConvert extends AppCompatActivity {

    private ListView listViewSelectCryptoValute;
    private AdapterSelectCryptoValute adapterSelectCryptoValute;
    private CryptoValute dataCryptoValute = null;
    private double priceCryptoValute = 0;
    private String symbolCryptoValute = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_select_cryptovalute_convert);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Выбрать криптовалюту");

        Bundle extras = getIntent().getExtras();
        dataCryptoValute = (CryptoValute) extras.getSerializable("dataCryptoValute");
        adapterSelectCryptoValute = new AdapterSelectCryptoValute(dataCryptoValute);
        listViewSelectCryptoValute = (ListView) findViewById(R.id.listview_select_cryptovalute);
        listViewSelectCryptoValute.setAdapter(adapterSelectCryptoValute);

        listViewSelectCryptoValute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //LinearLayout layout = view.findViewById(R.id.layout_list_select_cryptovalute);
                priceCryptoValute = dataCryptoValute.getData().get(i).getQuote().getUsdDataCoin().getPrice();
                symbolCryptoValute = dataCryptoValute.getData().get(i).getSymbol();
                Intent data = new Intent();
                data.putExtra(ActivityConvertCryptoValute.PRICE_MESSAGE, priceCryptoValute);
                data.putExtra(ActivityConvertCryptoValute.SYMBOL_MESSAGE, symbolCryptoValute);
                setResult(RESULT_OK, data);
                finish();
            }
        });
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
