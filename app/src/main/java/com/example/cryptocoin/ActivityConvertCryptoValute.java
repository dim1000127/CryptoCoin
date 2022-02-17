package com.example.cryptocoin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.metadatapojo.Metadata;
import com.example.cryptocoin.retrofit.RetrofitSingleton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityConvertCryptoValute extends AppCompatActivity {

    public static final String PRICE_MESSAGE="PRICE_MESSAGE";
    public static final String SYMBOL_MESSAGE="SYMBOL_MESSAGE";
    public static final String ID_MESSAGE="ID_MESSAGE";
    public static final String ASSET_NUMBER = "CONVERTNUMBER";
    public static final String TYPE_ASSET = "ASSET";

    private Subscription subscription;

    private TextView textViewSymbolFirstAsset;
    private TextView textViewSymbolSecondAsset;
    private ImageView imageViewLogoFirstAsset;
    private ImageView imageViewLogoSecondAsset;
    private EditText editTextValueFirstAsset;
    private EditText editTextValueSecondAsset;
    private LinearLayout layoutConvertFirstAsset;
    private LinearLayout layoutConvertSecondAsset;

    private CryptoValute dataCryptoValute = null;
    private Metadata metadata = null;

    private double priceFirstAsset = 0;
    private double priceSecondAsset = 0;
    private String idCryptoValuteAsset = "1";
    private String symbolMessageFirstAsset = null;
    private String symbolMessageSecondAsset = null;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        editTextValueFirstAsset.setText("0");
                        editTextValueSecondAsset.setText("0");
                        if (intent.getStringExtra(ASSET_NUMBER).equals(Const.CONVERT_FIRST)) {
                            if (intent.getStringExtra(TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                                symbolMessageFirstAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                                priceFirstAsset = intent.getDoubleExtra(PRICE_MESSAGE, 0);
                                idCryptoValuteAsset = String.valueOf(intent.getIntExtra(ID_MESSAGE, 1));
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                                Picasso.get().load(metadata.getData().get(idCryptoValuteAsset).getLogo()).into(imageViewLogoFirstAsset);

                            } else if (intent.getStringExtra(TYPE_ASSET).equals(Const.FIAT)) {
                                symbolMessageFirstAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                                if(symbolMessageFirstAsset.equals(Const.USD_SYMBOL)){
                                    textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                                    Picasso.get().load(R.drawable.usd_logo).into(imageViewLogoFirstAsset);
                                }
                                else if(symbolMessageFirstAsset.equals(Const.RUB_SYMBOL)){
                                    textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                                    Picasso.get().load(R.drawable.rub_logo).into(imageViewLogoFirstAsset);
                                }
                            }
                        }
                        else if (intent.getStringExtra(ASSET_NUMBER).equals(Const.CONVERT_SECOND)) {
                            if (intent.getStringExtra(TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                                symbolMessageSecondAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                                priceSecondAsset = intent.getDoubleExtra(PRICE_MESSAGE, 0);
                                idCryptoValuteAsset = String.valueOf(intent.getIntExtra(ID_MESSAGE, 1));
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                                Picasso.get().load(metadata.getData().get(idCryptoValuteAsset).getLogo()).into(imageViewLogoSecondAsset);

                            } else if (intent.getStringExtra(TYPE_ASSET).equals(Const.FIAT)) {
                                symbolMessageSecondAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                                if(symbolMessageSecondAsset.equals(Const.USD_SYMBOL)){
                                    textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                                    Picasso.get().load(R.drawable.usd_logo).into(imageViewLogoSecondAsset);
                                }
                                else if(symbolMessageSecondAsset.equals(Const.RUB_SYMBOL)){
                                    textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                                    Picasso.get().load(R.drawable.rub_logo).into(imageViewLogoSecondAsset);
                                }

                            }
                        }
                    }
                }
            });

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_cv);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Конвертер валют");

        layoutConvertFirstAsset = (LinearLayout) findViewById(R.id.view_select_first_asset);
        layoutConvertSecondAsset = (LinearLayout) findViewById(R.id.view_select_second_asset);
        imageViewLogoFirstAsset = (ImageView) findViewById(R.id.image_logo_first_asset);
        imageViewLogoSecondAsset = (ImageView) findViewById(R.id.image_logo_second_asset);
        textViewSymbolFirstAsset = (TextView) findViewById(R.id.tv_symbol_first_asset);
        textViewSymbolSecondAsset = (TextView) findViewById(R.id.tv_symbol_second_asset);
        editTextValueFirstAsset = (EditText) findViewById(R.id.et_value_first_asset);
        editTextValueSecondAsset = (EditText) findViewById(R.id.et_value_second_asset);

        editTextValueFirstAsset.setText("0");
        editTextValueSecondAsset.setText("0");

        layoutConvertFirstAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCryptoSelect(Const.CONVERT_FIRST);
            }
        });

        layoutConvertSecondAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCryptoSelect(Const.CONVERT_SECOND);
            }
        });

        editTextValueFirstAsset.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                /*if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextCvStr = editTextValueFirstAsset.getText().toString();
                    if(valueEditTextCvStr.isEmpty() == false) {
                        double valEtCryptoValute = Double.parseDouble(editTextValueFirstAsset.getText().toString());
                        double calculateVal = valEtCryptoValute * priceFirstAsset;
                        if (calculateVal >= 1) {
                            editTextValueSecondAsset.setText(String.format("%.2f", calculateVal).replace(",", "."));
                        } else {
                            editTextValueSecondAsset.setText(String.format("%.6f", calculateVal).replace(",", "."));
                        }
                    }
                }*/
                return false;
            }
        });

        editTextValueSecondAsset.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                /*if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&(i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextUsdStr = editTextValueSecondAsset.getText().toString();
                    if(valueEditTextUsdStr.isEmpty() == false) {
                        double valEtUsd = Double.parseDouble(editTextValueSecondAsset.getText().toString());
                        double calculateVal = valEtUsd / priceFirstAsset;
                        if (calculateVal >= 1) {
                            editTextValueFirstAsset.setText(String.format("%.2f", calculateVal).replace(",", "."));
                        } else {
                            editTextValueFirstAsset.setText(String.format("%.6f", calculateVal).replace(",", "."));
                        }
                    }
                }*/
                return false;
            }
        });

        editTextValueFirstAsset.setFilters(new InputFilter[] {new DecimalFilter(6)});
        editTextValueSecondAsset.setFilters(new InputFilter[] {new DecimalFilter(6)});

        getCryptoValuteData();
    }

    private void convertCryptoSelect(String numberCryptoConvert){
        if(dataCryptoValute!=null && metadata != null){
            Intent intent = new Intent(ActivityConvertCryptoValute.this, ActivitySelectCryptoValuteConvert.class);
            intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
            intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
            intent.putExtra(Const.ASSET_NUMBER_INTENT, numberCryptoConvert);
            mStartForResult.launch(intent);
        }
    }

    private void getCryptoValuteData(){
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = RetrofitSingleton.getCryptoValuteObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, Object> _cryptoValuteMetadata) {
                        CryptoValute _cryptoValute = (CryptoValute) _cryptoValuteMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata _metadata = (Metadata) _cryptoValuteMetadata.get(Const.METADATA_KEY_MAP);
                        dataCryptoValute = _cryptoValute;
                        metadata = _metadata;
                        textViewSymbolFirstAsset.setText(dataCryptoValute.getData().get(0).getSymbol());
                        textViewSymbolSecondAsset.setText(Const.USD_SYMBOL);
                        priceFirstAsset = dataCryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice();
                        String idCryptoConvert = String.valueOf(dataCryptoValute.getData().get(0).getId());
                        Picasso.get().load(metadata.getData().get(idCryptoConvert).getLogo()).into(imageViewLogoFirstAsset);
                        Picasso.get().load(R.drawable.usd_logo).into(imageViewLogoSecondAsset);
                    }
                });
    }

    @Override
    public void onDestroy() {
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
