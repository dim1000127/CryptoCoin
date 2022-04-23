package com.example.cryptocoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.DecimalFilter;
import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.retrofit.RetrofitQuotesConvertSingleton;
import com.example.cryptocoin.retrofit.RetrofitQuotesSingleton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConvertCryptoValute extends AppCompatActivity implements View.OnClickListener {



    private Subscription subscriptionQuotes;
    private Subscription subscriptionQuotesConvert;

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
    private ImageButton buttonCalculateConvertBackspace;

    //private QuotesCryptoValute quotesCryptoValute = null;
    //private Metadata metadata = null;
    private SharedPreferences mSettings;

    public String idFirstAsset = null;
    public String idSecondAsset = null;
    private double priceFirstAsset = 1;
    private double priceSecondAsset = 1;
    private double dollarPrice = 1;
    //private double rublePrice = 75;
    private String idCryptoValuteAsset = "1";
    private String symbolMessageFirstAsset = null;
    private String symbolMessageSecondAsset = null;
    private boolean isConvertFirst = false;
    private boolean isConvertSecond = false;

    ActivityResultLauncher<Intent> firstStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if (intent.getStringExtra(Const.TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                            imageViewLogoFirstAsset.setImageResource(R.drawable.ic_square_view);
                            textViewSymbolFirstAsset.setText("");
                            priceFirstAsset = 1;
                            getQuotesCVData(intent.getStringExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE),true, false);
                        } else if (intent.getStringExtra(Const.TYPE_ASSET).equals(Const.FIAT)) {
                            symbolMessageFirstAsset = intent.getStringExtra(Const.SYMBOL_MESSAGE);
                            if(symbolMessageFirstAsset.equals(Const.USD_SYMBOL)){
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);
                                priceFirstAsset = dollarPrice;
                                idFirstAsset = Const.USD_SYMBOL;
                                Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoFirstAsset);
                                convertationFirst();
                            }
                            else if(symbolMessageFirstAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);
                                //priceFirstAsset = dollarPrice / rublePrice;
                                idFirstAsset = Const.RUB_SYMBOL;
                                Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoFirstAsset);
                                convertationFirst();
                            }
                        }
                    }
                    editTextValueFirstAsset.requestFocus();
                    editTextValueFirstAsset.setSelectAllOnFocus(true);
                }
            });

    ActivityResultLauncher<Intent> secondStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if (intent.getStringExtra(Const.TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                            imageViewLogoSecondAsset.setImageResource(R.drawable.ic_square_view);
                            textViewSymbolSecondAsset.setText("");
                            priceSecondAsset = 1;
                            getQuotesCVData(intent.getStringExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE), false, true);

                        } else if (intent.getStringExtra(Const.TYPE_ASSET).equals(Const.FIAT)) {
                            symbolMessageSecondAsset = intent.getStringExtra(Const.SYMBOL_MESSAGE);
                            if(symbolMessageSecondAsset.equals(Const.USD_SYMBOL)){
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);
                                priceSecondAsset = dollarPrice;
                                idSecondAsset = Const.USD_SYMBOL;
                                Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
                                convertationSecond();
                            }
                            else if(symbolMessageSecondAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);
                                //priceSecondAsset = dollarPrice/rublePrice;
                                idSecondAsset = Const.RUB_SYMBOL;
                                Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoSecondAsset);
                                convertationSecond();
                            }
                        }
                    }
                    editTextValueSecondAsset.requestFocus();
                    editTextValueSecondAsset.setSelectAllOnFocus(true);
                }
            });

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_cv);

        Toolbar toolbar =findViewById(R.id.toolbar_convert_cv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Конвертер валют");

        mSettings = getSharedPreferences(Const.APP_PREFERENCES, Context.MODE_PRIVATE);
        idFirstAsset = mSettings.getString(Const.APP_PREFERENCES_ID_CV_FIRST_ASSET, "1");
        idSecondAsset = mSettings.getString(Const.APP_PREFERENCES_ID_CV_SECOND_ASSET, "USD");

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
        buttonCalculateConvertBackspace = (ImageButton) findViewById(R.id.btn_calculate_convert_backspace);

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

        editTextValueFirstAsset.setShowSoftInputOnFocus(false);
        editTextValueSecondAsset.setShowSoftInputOnFocus(false);

        editTextValueFirstAsset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isConvertSecond) {
                    convertationFirst();
                }
            }
        });

        editTextValueSecondAsset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(isConvertFirst) {
                    convertationSecond();
                }
            }
        });

        editTextValueFirstAsset.setFilters(new InputFilter[] {new DecimalFilter()});
        editTextValueSecondAsset.setFilters(new InputFilter[] {new DecimalFilter()});
        editTextValueFirstAsset.setText("0");
        editTextValueSecondAsset.setText("0");



        Intent intent = getIntent();
        boolean isStartFromBottomSheet = intent.getBooleanExtra(Const.START_FROM_BOTTOMSHEET, false);
        if(isStartFromBottomSheet){
            idFirstAsset = intent.getStringExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE);
            priceFirstAsset = intent.getDoubleExtra(Const.PRICE_MESSAGE, 1);
            textViewSymbolFirstAsset.setText(intent.getStringExtra(Const.SYMBOL_MESSAGE));
            Picasso.get()
                    .load(intent.getStringExtra(Const.LOGO_MESSAGE))
                    .into(imageViewLogoFirstAsset);
            convertationFirst();

            idSecondAsset = Const.USD_SYMBOL;
            textViewSymbolSecondAsset.setText(Const.USD_SYMBOL);
            priceSecondAsset = 1;
            Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
            editTextValueFirstAsset.requestFocus();
            editTextValueFirstAsset.setSelectAllOnFocus(true);
        }
        else{
            if(mSettings.contains(Const.APP_PREFERENCES_ID_CV_FIRST_ASSET) &&
                    mSettings.contains(Const.APP_PREFERENCES_ID_CV_SECOND_ASSET)) {
                String firstAsset = mSettings.getString(Const.APP_PREFERENCES_ID_CV_FIRST_ASSET, "1");
                String secondAsset = mSettings.getString(Const.APP_PREFERENCES_ID_CV_SECOND_ASSET, "USD");
                if(firstAsset.equals(Const.USD_SYMBOL)){
                    idFirstAsset = Const.USD_SYMBOL;
                    textViewSymbolFirstAsset.setText(Const.USD_SYMBOL);
                    priceFirstAsset = 1;
                    Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoFirstAsset);
                }else if(firstAsset.equals(Const.RUB_SYMBOL)){
                    idFirstAsset = Const.RUB_SYMBOL;
                    textViewSymbolFirstAsset.setText(Const.RUB_SYMBOL);
                    //priceFirstAsset = 1;
                    Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoFirstAsset);
                }

                if(secondAsset.equals(Const.USD_SYMBOL)){
                    idSecondAsset = Const.USD_SYMBOL;
                    textViewSymbolSecondAsset.setText(Const.USD_SYMBOL);
                    priceSecondAsset = 1;
                    Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
                }else if(secondAsset.equals(Const.RUB_SYMBOL)){
                    idSecondAsset = Const.RUB_SYMBOL;
                    textViewSymbolSecondAsset.setText(Const.RUB_SYMBOL);
                    //priceSecondAsset = 1;
                    Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoSecondAsset);
                }

                //String idStrQuery = "";
                if (!firstAsset.equals(Const.USD_SYMBOL) && !firstAsset.equals(Const.RUB_SYMBOL)&&
                !secondAsset.equals(Const.USD_SYMBOL) && !secondAsset.equals(Const.RUB_SYMBOL)){
                    String idStrQuery = firstAsset + "," + secondAsset;
                    getQuotesConvertCVData(idStrQuery, true, true);

                }
                else if (!firstAsset.equals(Const.USD_SYMBOL) && !firstAsset.equals(Const.RUB_SYMBOL)){
                    String idStrQuery = firstAsset;
                    getQuotesConvertCVData(idStrQuery, true, false);
                }
                else if(!secondAsset.equals(Const.USD_SYMBOL) && !secondAsset.equals(Const.RUB_SYMBOL)){
                    String idStrQuery = secondAsset;
                    getQuotesConvertCVData(idStrQuery, false, true);
                }
            }
            else{
                getQuotesCVData("1", true, false);
                idSecondAsset = Const.USD_SYMBOL;
                textViewSymbolSecondAsset.setText(Const.USD_SYMBOL);
                priceSecondAsset = 1;
                Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
            }
        }
    }

    private void getQuotesCVData(String id, boolean firstAsset, boolean secondAsset) {
        if (subscriptionQuotes != null && !subscriptionQuotes.isUnsubscribed()){
            subscriptionQuotes.unsubscribe();
        }

        subscriptionQuotes = RetrofitQuotesSingleton.getQuotesCVObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(layoutConvertFirstAsset, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(Map<String, Object> _quotesCVMetadata) {
                        QuotesCryptoValute quotesCryptoValute = (QuotesCryptoValute) _quotesCVMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata metadata = (Metadata) _quotesCVMetadata.get(Const.METADATA_KEY_MAP);

                        if (firstAsset){
                            symbolMessageFirstAsset = quotesCryptoValute.getData().get(id).getSymbol();
                            priceFirstAsset = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(id).getLogo())
                                    .into(imageViewLogoFirstAsset);
                            convertationFirst();
                            idFirstAsset = id;
                        }
                        if(secondAsset){
                            symbolMessageSecondAsset = quotesCryptoValute.getData().get(id).getSymbol();
                            priceSecondAsset = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(id).getLogo())
                                    .into(imageViewLogoSecondAsset);
                            convertationSecond();
                            idSecondAsset = id;
                        }
                    }
                });
    }

    private void getQuotesConvertCVData(String id, boolean firstAsset, boolean secondAsset) {
        if (subscriptionQuotesConvert != null && !subscriptionQuotesConvert.isUnsubscribed()){
            subscriptionQuotesConvert.unsubscribe();
        }

        subscriptionQuotesConvert = RetrofitQuotesConvertSingleton.getQuotesConvertCVObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(layoutConvertFirstAsset, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(Map<String, Object> _quotesCVMetadata) {
                        QuotesCryptoValute quotesCryptoValute = (QuotesCryptoValute) _quotesCVMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        Metadata metadata = (Metadata) _quotesCVMetadata.get(Const.METADATA_KEY_MAP);

                        if (firstAsset && !secondAsset){
                            symbolMessageFirstAsset = quotesCryptoValute.getData().get(id).getSymbol();
                            priceFirstAsset = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(id).getLogo())
                                    .into(imageViewLogoFirstAsset);
                            convertationFirst();
                            idFirstAsset = id;
                        }
                        if(!firstAsset && secondAsset){
                            symbolMessageSecondAsset = quotesCryptoValute.getData().get(id).getSymbol();
                            priceSecondAsset = quotesCryptoValute.getData().get(id).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(id).getLogo())
                                    .into(imageViewLogoSecondAsset);
                            convertationSecond();
                            idSecondAsset = id;
                        }
                        if(firstAsset && secondAsset){
                            String[] idStr = id.split(",");

                            symbolMessageFirstAsset = quotesCryptoValute.getData().get(idStr[0]).getSymbol();
                            priceFirstAsset = quotesCryptoValute.getData().get(idStr[0]).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(idStr[0]).getLogo())
                                    .into(imageViewLogoFirstAsset);
                            convertationFirst();
                            idFirstAsset = idStr[0];

                            symbolMessageSecondAsset = quotesCryptoValute.getData().get(idStr[1]).getSymbol();
                            priceSecondAsset = quotesCryptoValute.getData().get(idStr[1]).getQuote().getUsdDataCoin().getPrice();
                            textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                            Picasso.get()
                                    .load(metadata.getData().get(idStr[1]).getLogo())
                                    .into(imageViewLogoSecondAsset);
                            convertationSecond();
                            idSecondAsset = idStr[1];
                        }
                        editTextValueFirstAsset.requestFocus();
                        editTextValueFirstAsset.setSelectAllOnFocus(true);
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
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("1");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("1");
                }
                break;
            case R.id.btn_calculate_convert_2:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("2");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("2");
                }
                break;
            case R.id.btn_calculate_convert_3:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("3");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("3");
                }
                break;
            case R.id.btn_calculate_convert_4:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("4");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("4");
                }
                break;
            case R.id.btn_calculate_convert_5:
                if (editTextValueFirstAsset.hasFocus() ){
                    updateTextFirstAsset("5");
                }
                else if(editTextValueSecondAsset.hasFocus() ){
                    updateTextSecondAsset("5");
                }
                break;
            case R.id.btn_calculate_convert_6:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("6");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("6");
                }
                break;
            case R.id.btn_calculate_convert_7:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("7");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("7");
                }
                break;
            case R.id.btn_calculate_convert_8:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("8");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("8");
                }
                break;
            case R.id.btn_calculate_convert_9:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("9");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("9");
                }
                break;
            case R.id.btn_calculate_convert_0:
                if (editTextValueFirstAsset.hasFocus()){
                    updateTextFirstAsset("0");
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    updateTextSecondAsset("0");
                }
                break;
            case R.id.btn_calculate_convert_comma:
                if (editTextValueFirstAsset.hasFocus()){
                    if(!editTextValueFirstAsset.getText().toString().contains(",")) {
                        updateTextFirstAsset(",");
                    }
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    if(!editTextValueSecondAsset.getText().toString().contains(",")) {
                        updateTextSecondAsset(",");
                    }
                }
                break;
            case R.id.btn_calculate_convert_backspace:
                if (editTextValueFirstAsset.hasFocus()){
                    isConvertSecond = true;
                    int cursorPosStart = editTextValueFirstAsset.getSelectionStart();
                    int cursorPosEnd = editTextValueFirstAsset.getSelectionEnd();
                    int textLenght = editTextValueFirstAsset.getText().length();
                    if(cursorPosStart != 0 && cursorPosEnd == cursorPosStart && textLenght != 0){
                        SpannableStringBuilder selection = (SpannableStringBuilder) editTextValueFirstAsset.getText();
                        selection.replace(cursorPosEnd-1, cursorPosStart, "");
                        String strSelection = selection.toString().replaceFirst("^0*", "");
                        if(strSelection.indexOf(",")==0){
                            strSelection = "0"+strSelection;
                        }
                        editTextValueFirstAsset.setText(strSelection);
                        if(selection.length()==strSelection.length()) {
                            editTextValueFirstAsset.setSelection(cursorPosStart-1);
                        }
                    }
                    else if(cursorPosEnd != cursorPosStart && textLenght != 0){
                        SpannableStringBuilder selection = (SpannableStringBuilder) editTextValueFirstAsset.getText();
                        selection.replace(cursorPosStart, cursorPosEnd, "");
                        String strSelection = selection.toString().replaceFirst("^0*", "");
                        if(strSelection.indexOf(",")==0){
                            strSelection = "0"+strSelection;
                        }
                        editTextValueFirstAsset.setText(strSelection);
                    }
                }
                else if(editTextValueSecondAsset.hasFocus()){
                    isConvertFirst = true;
                    int cursorPosStart = editTextValueSecondAsset.getSelectionStart();
                    int cursorPosEnd = editTextValueSecondAsset.getSelectionEnd();
                    int textLenght = editTextValueSecondAsset.getText().length();
                    if(cursorPosStart != 0 && cursorPosEnd == cursorPosStart && textLenght != 0){
                        SpannableStringBuilder selection = (SpannableStringBuilder) editTextValueSecondAsset.getText();
                        selection.replace(cursorPosEnd-1, cursorPosStart, "");
                        String strSelection = selection.toString().replaceFirst("^0*", "");
                        if(strSelection.indexOf(",")==0){
                            strSelection = "0"+strSelection;
                        }
                        editTextValueSecondAsset.setText(strSelection);
                        if(selection.length()==strSelection.length()) {
                            editTextValueSecondAsset.setSelection(cursorPosStart-1);
                        }
                    } else if(cursorPosEnd != cursorPosStart && textLenght != 0){
                        SpannableStringBuilder selection = (SpannableStringBuilder) editTextValueSecondAsset.getText();
                        selection.replace(cursorPosStart, cursorPosEnd, "");
                        String strSelection = selection.toString().replaceFirst("^0*", "");
                        if(strSelection.indexOf(",")==0){
                            strSelection = "0"+strSelection;
                        }
                        editTextValueSecondAsset.setText(strSelection);
                    }
                }
                break;
        }
    }

    private void convertationFirst(){
        String valueCvStr = editTextValueFirstAsset.getText().toString();
        if (!valueCvStr.isEmpty()) {
            if(!valueCvStr.equals(",")) {
                isConvertFirst = false;
                double valEtFirstAsset = Double.parseDouble(valueCvStr.replace(",", "."));
                double calculateVal = valEtFirstAsset * priceFirstAsset / priceSecondAsset;
                if (calculateVal >= 1) {
                    editTextValueSecondAsset.setText(String.format("%.2f", calculateVal));
                } else if (calculateVal == 0){
                    editTextValueSecondAsset.setText(String.format("%.0f", calculateVal));
                }else{
                    editTextValueSecondAsset.setText(String.format("%.6f", calculateVal));
                }
            }
        }
        else{
            editTextValueSecondAsset.setText("0");
        }
    }

    private void convertationSecond(){
        String valueSecondAssetStr = editTextValueSecondAsset.getText().toString();
        if (!valueSecondAssetStr.isEmpty()) {
            if(!valueSecondAssetStr.equals(",")) {
                isConvertSecond = false;
                double valEtSecondAsset = Double.parseDouble(valueSecondAssetStr.replace(",", "."));
                double calculateVal = valEtSecondAsset / priceFirstAsset * priceSecondAsset;
                if (calculateVal >= 1) {
                    editTextValueFirstAsset.setText(String.format("%.2f", calculateVal));
                } else if (calculateVal == 0){
                    editTextValueFirstAsset.setText(String.format("%.0f", calculateVal));
                }else{
                    editTextValueFirstAsset.setText(String.format("%.6f", calculateVal));
                }
            }
        }
        else{
            editTextValueFirstAsset.setText("0");
        }
    }

    private void convertFirstAssetSelect(){
        Intent intent = new Intent(ConvertCryptoValute.this, SelectCryptoValuteConvert.class);
        firstStartForResult.launch(intent);
    }

    private void convertSecondAssetSelect(){
        Intent intent = new Intent(ConvertCryptoValute.this, SelectCryptoValuteConvert.class);
        secondStartForResult.launch(intent);
    }


    private void updateTextFirstAsset(String strToAdd){
        isConvertSecond = true;
        String oldStr = editTextValueFirstAsset.getText().toString();
        int cursorPosStart = editTextValueFirstAsset.getSelectionStart();
        int cursorPosEnd = editTextValueFirstAsset.getSelectionEnd();
        String leftStr = oldStr.substring(0, cursorPosStart);
        String rightStr = oldStr.substring(cursorPosEnd);

        if(leftStr.equals("0") && oldStr.equals("0")){
            leftStr = "";
        }

        if(leftStr.equals("") && strToAdd.equals(",")){
            strToAdd = "0,";
        }

        String newStr = leftStr+strToAdd+rightStr;

        if(newStr.indexOf(",")==0){
            newStr = "0"+newStr;
        }

        if(checkStrCorrect(newStr)){
            editTextValueFirstAsset.setText(newStr);
            if(strToAdd.equals("0,")) {
                editTextValueFirstAsset.setSelection(leftStr.length() + 2);
            }
            else{
                editTextValueFirstAsset.setSelection(leftStr.length() + 1);
            }
        }
        else {
            editTextValueFirstAsset.setText(oldStr);
            editTextValueFirstAsset.setSelection(cursorPosStart);
        }
    }

    private void updateTextSecondAsset(String strToAdd){
        isConvertFirst = true;
        String oldStr = editTextValueSecondAsset.getText().toString();
        int cursorPosStart = editTextValueSecondAsset.getSelectionStart();
        int cursorPosEnd = editTextValueSecondAsset.getSelectionEnd();
        String leftStr = oldStr.substring(0, cursorPosStart);
        String rightStr = oldStr.substring(cursorPosEnd);

        if(leftStr.equals("0") && oldStr.equals("0")){
            leftStr = "";
        }

        if(leftStr.equals("") && strToAdd.equals(",")){
            strToAdd = "0,";
        }

        String newStr = leftStr+strToAdd+rightStr;

        if(newStr.indexOf(",")==0){
            newStr = "0"+newStr;
        }

        if(checkStrCorrect(newStr)){
            editTextValueSecondAsset.setText(newStr);
            if(strToAdd.equals("0,")) {
                editTextValueSecondAsset.setSelection(leftStr.length() + 2);
            }
            else{
                editTextValueSecondAsset.setSelection(leftStr.length() + 1);
            }
        }
        else {
            editTextValueSecondAsset.setText(oldStr);
            editTextValueSecondAsset.setSelection(cursorPosStart);
        }
    }

    private boolean checkStrCorrect(String str){ ;
        if (str.matches(Const.REGEX_EDITTEXT_CONVERT)) { return true; }
        else { return false; }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(Const.APP_PREFERENCES_ID_CV_FIRST_ASSET, idFirstAsset);
        editor.putString(Const.APP_PREFERENCES_ID_CV_SECOND_ASSET, idSecondAsset);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscriptionQuotes != null && !subscriptionQuotes.isUnsubscribed()) {
            subscriptionQuotes.unsubscribe();
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
