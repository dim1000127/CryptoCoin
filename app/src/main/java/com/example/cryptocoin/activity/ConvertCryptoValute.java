package com.example.cryptocoin.activity;

import android.app.Activity;
import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.DecimalFilter;
import com.example.cryptocoin.R;
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

public class ConvertCryptoValute extends AppCompatActivity implements View.OnClickListener {

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
    private ImageButton buttonCalculateConvertBackspace;

    private CryptoValute dataCryptoValute = null;
    private Metadata metadata = null;

    private double priceFirstAsset = 11;
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
                        if (intent.getStringExtra(TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                            symbolMessageFirstAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                            priceFirstAsset = intent.getDoubleExtra(PRICE_MESSAGE, 0);
                            idCryptoValuteAsset = String.valueOf(intent.getIntExtra(ID_MESSAGE, 1));
                            textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);

                            Picasso.get().load(metadata.getData().get(idCryptoValuteAsset).getLogo()).into(imageViewLogoFirstAsset);
                            convertationFirst();
                        } else if (intent.getStringExtra(TYPE_ASSET).equals(Const.FIAT)) {
                            symbolMessageFirstAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                            if(symbolMessageFirstAsset.equals(Const.USD_SYMBOL)){
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);
                                priceFirstAsset = dollarPrice;

                                Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoFirstAsset);
                                convertationFirst();
                            }
                            else if(symbolMessageFirstAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolFirstAsset.setText(symbolMessageFirstAsset);
                                //priceFirstAsset = dollarPrice / rublePrice;
                                Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoFirstAsset);
                                convertationFirst();
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
                        if (intent.getStringExtra(TYPE_ASSET).equals(Const.CRYPTOVALUTE)) {

                            symbolMessageSecondAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                            priceSecondAsset = intent.getDoubleExtra(PRICE_MESSAGE, 0);
                            idCryptoValuteAsset = String.valueOf(intent.getIntExtra(ID_MESSAGE, 1));
                            textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);

                            Picasso.get().load(metadata.getData().get(idCryptoValuteAsset).getLogo()).into(imageViewLogoSecondAsset);
                            convertationSecond();
                        } else if (intent.getStringExtra(TYPE_ASSET).equals(Const.FIAT)) {
                            symbolMessageSecondAsset = intent.getStringExtra(SYMBOL_MESSAGE);
                            if(symbolMessageSecondAsset.equals(Const.USD_SYMBOL)){
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);
                                priceSecondAsset = dollarPrice;
                                Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
                                convertationSecond();
                            }
                            else if(symbolMessageSecondAsset.equals(Const.RUB_SYMBOL)){
                                textViewSymbolSecondAsset.setText(symbolMessageSecondAsset);
                                //priceSecondAsset = dollarPrice/rublePrice;
                                Picasso.get().load(R.drawable.ic_rub_logo).into(imageViewLogoSecondAsset);
                                convertationSecond();
                            }
                        }
                    }
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
        //editTextValueFirstAsset.setSelection(editTextValueFirstAsset.getText().length());
        editTextValueFirstAsset.requestFocus();
        editTextValueFirstAsset.setSelectAllOnFocus(true);

        getCryptoValuteData();
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
                        Picasso.get().load(R.drawable.ic_usd_logo).into(imageViewLogoSecondAsset);
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
        if(dataCryptoValute!=null && metadata != null){
            Intent intent = new Intent(ConvertCryptoValute.this, SelectCryptoValuteConvert.class);
            intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
            intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
            //intent.putExtra(Const.ASSET_NUMBER_INTENT, numberCryptoConvert);
            firstStartForResult.launch(intent);
        }
    }

    private void convertSecondAssetSelect(){
        if(dataCryptoValute!=null && metadata != null){
            Intent intent = new Intent(ConvertCryptoValute.this, SelectCryptoValuteConvert.class);
            intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
            intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
            //intent.putExtra(Const.ASSET_NUMBER_INTENT, numberCryptoConvert);
            secondStartForResult.launch(intent);
        }
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
