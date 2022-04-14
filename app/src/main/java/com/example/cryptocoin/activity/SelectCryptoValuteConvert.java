package com.example.cryptocoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.SelectCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.retrofit.RetrofitIdSingleton;
import com.google.android.material.snackbar.Snackbar;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectCryptoValuteConvert extends AppCompatActivity {

    private Subscription subscription;

    private ListView listViewSelectCryptoValute;
    private SelectCryptoValute adapterSelectCryptoValute;
    private LinearLayout layoutFiatUsdSelect;
    private LinearLayout layoutFiatRubSelect;
    private SearchView searchView;

    private IdCryptoValute idCryptoValute = null;

    private String symbolFiat = null;
    private String symbolCryptoValute = null;
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
        layoutFiatUsdSelect = (LinearLayout) findViewById(R.id.layout_fiat_usd_select);
        layoutFiatRubSelect = (LinearLayout) findViewById(R.id.layout_fiat_rub_select);
        listViewSelectCryptoValute = (ListView) findViewById(R.id.listview_select_cryptovalute);

        listViewSelectCryptoValute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeAsset = Const.CRYPTOVALUTE;
                LinearLayout layout = view.findViewById(R.id.layout_list_select_cryptovalute);
                String id = layout.getTag().toString();

                Intent data = new Intent();
                data.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, id);
                data.putExtra(Const.TYPE_ASSET, typeAsset);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(idCryptoValute!=null){
                    adapterSelectCryptoValute.getFilter().filter(s);
                }

                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.background_searchview, null));
                searchView.setIconifiedByDefault(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primaryColor, null));
                return false;
            }
        });

        getIdCryptoValute();
    }

    private void getIdCryptoValute() {
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = RetrofitIdSingleton.getIdCryptoValuteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IdCryptoValute>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(layoutFiatRubSelect, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(IdCryptoValute _idCryptoValute) {
                        idCryptoValute = _idCryptoValute;
                        adapterSelectCryptoValute = new SelectCryptoValute(idCryptoValute);
                        listViewSelectCryptoValute.setAdapter(adapterSelectCryptoValute);
                    }
                });
    }

    private void selecetFiatUsd(){
        symbolFiat = Const.USD_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(Const.TYPE_ASSET, typeAsset);
        data.putExtra(Const.SYMBOL_MESSAGE, symbolFiat);
        setResult(RESULT_OK, data);
        finish();
    }

    private void selectFiatRub(){
        symbolFiat = Const.RUB_SYMBOL;
        typeAsset = Const.FIAT;
        Intent data = new Intent();
        data.putExtra(Const.TYPE_ASSET, typeAsset);
        data.putExtra(Const.SYMBOL_MESSAGE, symbolFiat);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
