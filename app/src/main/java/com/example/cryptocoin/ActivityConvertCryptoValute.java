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

import java.io.Serializable;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityConvertCryptoValute extends AppCompatActivity {

    public static final String PRICE_MESSAGE="PRICE_MESSAGE";
    public static final String SYMBOL_MESSAGE="SYMBOL_MESSAGE";

    private Subscription subscription;

    private TextView textViewSymbolCryptoValute;
    private EditText editTextValueCryptoValute;
    private EditText editTextValueUsd;
    private CryptoValute dataCryptoValute = null;
    private Metadata metadata = null;
    private double priceCryptoValute = 0;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        String symbolMessage = intent.getStringExtra(SYMBOL_MESSAGE);
                        textViewSymbolCryptoValute.setText(symbolMessage);
                        priceCryptoValute = intent.getDoubleExtra(PRICE_MESSAGE,0);
                        editTextValueCryptoValute.setText("0");
                        editTextValueUsd.setText("0");
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
        textViewSymbolCryptoValute = (TextView) findViewById(R.id.tv_symbol_cryptovalute);
        editTextValueCryptoValute = (EditText) findViewById(R.id.et_value_cryptovalute);
        editTextValueUsd = (EditText) findViewById(R.id.et_value_usd);
        editTextValueCryptoValute.setText("0");
        editTextValueUsd.setText("0");
        textViewSymbolCryptoValute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataCryptoValute!=null){
                    Intent intent = new Intent(ActivityConvertCryptoValute.this, ActivitySelectCryptoValuteConvert.class);
                    intent.putExtra(Const.CRYPTOVALUTE_INTENT, (Serializable) dataCryptoValute);
                    intent.putExtra(Const.METADATA_INTENT, (Serializable) metadata);
                    mStartForResult.launch(intent);
                }
            }
        });
        editTextValueCryptoValute.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextCvStr = editTextValueCryptoValute.getText().toString();
                    if(valueEditTextCvStr.isEmpty() == false) {
                        double valEtCryptoValute = Double.parseDouble(editTextValueCryptoValute.getText().toString());
                        double calculateVal = valEtCryptoValute * priceCryptoValute;
                        if (calculateVal >= 1) {
                            editTextValueUsd.setText(String.format("%.2f", calculateVal).replace(",", "."));
                        } else {
                            editTextValueUsd.setText(String.format("%.6f", calculateVal).replace(",", "."));
                        }
                    }
                }
                return false;
            }
        });
        editTextValueUsd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&(i == KeyEvent.KEYCODE_ENTER))
                {
                    String valueEditTextUsdStr = editTextValueUsd.getText().toString();
                    if(valueEditTextUsdStr.isEmpty() == false) {
                        double valEtUsd = Double.parseDouble(editTextValueUsd.getText().toString());
                        double calculateVal = valEtUsd / priceCryptoValute;
                        if (calculateVal >= 1) {
                            editTextValueCryptoValute.setText(String.format("%.2f", calculateVal).replace(",", "."));
                        } else {
                            editTextValueCryptoValute.setText(String.format("%.6f", calculateVal).replace(",", "."));
                        }
                    }
                }
                return false;
            }
        });

        editTextValueCryptoValute.setFilters(new InputFilter[] {new DecimalFilter(6)});
        editTextValueUsd.setFilters(new InputFilter[] {new DecimalFilter(6)});
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
                        textViewSymbolCryptoValute.setText(dataCryptoValute.getData().get(0).getSymbol());
                        priceCryptoValute = dataCryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice();
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
