package com.example.cryptocoin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ActivityConvertCryptoValute extends AppCompatActivity implements View.OnClickListener {

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
    private Button buttonCalculateConvert1;
    private Button buttonCalculateConvert2;
    private Button buttonCalculateConvert3;
    private Button buttonCalculateConvert4;
    private Button buttonCalculateConvert5;
    private Button buttonCalculateConvert6;
    private Button buttonCalculateConvert7;
    private Button buttonCalculateConvert8;
    private Button buttonCalculateConvert9;
    private Button buttonCalculateConvert0;
    private Button buttonCalculateConvertComma;
    private Button buttonCalculateConvertBackspace;

    private CryptoValute dataCryptoValute = null;
    private Metadata metadata = null;

    private double priceFirstAsset = 11;
    private double priceSecondAsset = 1;
    private double dollarPrice = 1;
    //private double rublePrice = 75;
    private String idCryptoValuteAsset = "1";
    private String symbolMessageFirstAsset = null;
    private String symbolMessageSecondAsset = null;

    ActivityResultLauncher<Intent> firstStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        editTextValueFirstAsset.setText("0");
                        editTextValueSecondAsset.setText("0");
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
                                priceFirstAsset = dollarPrice;

                                Picasso.get().load(R.drawable.usd_logo).into(imageViewLogoFirstAsset);
                            }
                            else if(symbolMessageFirstAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);
                                //priceFirstAsset = dollarPrice / rublePrice;
                                Picasso.get().load(R.drawable.rub_logo).into(imageViewLogoFirstAsset);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> secondStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        editTextValueFirstAsset.setText("0");
                        editTextValueSecondAsset.setText("0");

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
                                priceSecondAsset = dollarPrice;
                                Picasso.get().load(R.drawable.usd_logo).into(imageViewLogoSecondAsset);
                            }
                            else if(symbolMessageSecondAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);
                                //priceSecondAsset = dollarPrice/rublePrice;
                                Picasso.get().load(R.drawable.rub_logo).into(imageViewLogoSecondAsset);
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
        buttonCalculateConvert1 = (Button) findViewById(R.id.btn_calculate_convert_1);
        buttonCalculateConvert2 = (Button) findViewById(R.id.btn_calculate_convert_2);
        buttonCalculateConvert3 = (Button) findViewById(R.id.btn_calculate_convert_3);
        buttonCalculateConvert4 = (Button) findViewById(R.id.btn_calculate_convert_4);
        buttonCalculateConvert5 = (Button) findViewById(R.id.btn_calculate_convert_5);
        buttonCalculateConvert6 = (Button) findViewById(R.id.btn_calculate_convert_6);
        buttonCalculateConvert7 = (Button) findViewById(R.id.btn_calculate_convert_7);
        buttonCalculateConvert8 = (Button) findViewById(R.id.btn_calculate_convert_8);
        buttonCalculateConvert9 = (Button) findViewById(R.id.btn_calculate_convert_9);
        buttonCalculateConvert0 = (Button) findViewById(R.id.btn_calculate_convert_0);
        buttonCalculateConvertComma = (Button) findViewById(R.id.btn_calculate_convert_comma);
        buttonCalculateConvertBackspace = (Button) findViewById(R.id.btn_calculate_convert_backspace);

        buttonCalculateConvert1.setOnClickListener(this);
        buttonCalculateConvert2.setOnClickListener(this);
        buttonCalculateConvert3.setOnClickListener(this);
        buttonCalculateConvert4.setOnClickListener(this);
        buttonCalculateConvert5.setOnClickListener(this);
        buttonCalculateConvert6.setOnClickListener(this);
        buttonCalculateConvert7.setOnClickListener(this);
        buttonCalculateConvert8.setOnClickListener(this);
        buttonCalculateConvert9.setOnClickListener(this);
        buttonCalculateConvert0.setOnClickListener(this);
        buttonCalculateConvertComma.setOnClickListener(this);
        buttonCalculateConvertBackspace.setOnClickListener(this);
        layoutConvertFirstAsset.setOnClickListener(this);
        layoutConvertSecondAsset.setOnClickListener(this);

        editTextValueFirstAsset.setText("0");
        editTextValueSecondAsset.setText("0");

        editTextValueFirstAsset.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextCvStr = editTextValueFirstAsset.getText().toString();
                    if(valueEditTextCvStr.isEmpty() == false) {
                        double valEtFirstAsset = Double.parseDouble(valueEditTextCvStr.replace(",", "."));
                        double calculateVal = valEtFirstAsset * priceFirstAsset / priceSecondAsset;
                        if (calculateVal >= 1) {
                            editTextValueSecondAsset.setText(String.format("%.2f", calculateVal));
                        } else {
                            editTextValueSecondAsset.setText(String.format("%.6f", calculateVal));
                        }
                    }
                }
                return false;
            }
        });

        editTextValueSecondAsset.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&(i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextSecondAssetStr = editTextValueSecondAsset.getText().toString();
                    if(valueEditTextSecondAssetStr.isEmpty() == false) {
                        double valEtSecondAsset = Double.parseDouble(valueEditTextSecondAssetStr.replace(",", "."));
                        double calculateVal = valEtSecondAsset / priceFirstAsset * priceSecondAsset;
                        if (calculateVal >= 1) {
                            editTextValueFirstAsset.setText(String.format("%.2f", calculateVal));
                        } else {
                            editTextValueFirstAsset.setText(String.format("%.6f", calculateVal));
                        }
                    }
                }
                return false;
            }
        });

        editTextValueFirstAsset.setFilters(new InputFilter[] {new DecimalFilter(6)});
        editTextValueSecondAsset.setFilters(new InputFilter[] {new DecimalFilter(6)});

        getCryptoValuteData();
    }

    private void convertFirstAssetSelect(){
        if(dataCryptoValute!=null && metadata != null){
            Intent intent = new Intent(ActivityConvertCryptoValute.this, ActivitySelectCryptoValuteConvert.class);
            intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
            intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
            //intent.putExtra(Const.ASSET_NUMBER_INTENT, numberCryptoConvert);
            firstStartForResult.launch(intent);
        }
    }

    private void convertSecondAssetSelect(){
        if(dataCryptoValute!=null && metadata != null){
            Intent intent = new Intent(ActivityConvertCryptoValute.this, ActivitySelectCryptoValuteConvert.class);
            intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
            intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
            //intent.putExtra(Const.ASSET_NUMBER_INTENT, numberCryptoConvert);
            secondStartForResult.launch(intent);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_select_first_asset:
                convertFirstAssetSelect();
                break;
            case R.id.view_select_second_asset:
                convertSecondAssetSelect();
                break;
            case R.id.btn_calculate_convert_1:
                //Snackbar.make(buttonCalculateConvert1, R.string.soon, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.btn_calculate_convert_2:
                break;
            case R.id.btn_calculate_convert_3:
                break;
            case R.id.btn_calculate_convert_4:
                break;
            case R.id.btn_calculate_convert_5:
                break;
            case R.id.btn_calculate_convert_6:
                break;
            case R.id.btn_calculate_convert_7:
                break;
            case R.id.btn_calculate_convert_8:
                break;
            case R.id.btn_calculate_convert_9:
                break;
            case R.id.btn_calculate_convert_0:
                break;
            case R.id.btn_calculate_convert_comma:
                break;
            case R.id.btn_calculate_convert_backspace:
                break;
        }
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
