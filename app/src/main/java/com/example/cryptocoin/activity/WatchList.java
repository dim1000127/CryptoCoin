package com.example.cryptocoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.WatchListCV;
import com.example.cryptocoin.adapter.WatchListCVEmpty;
import com.example.cryptocoin.fragments.FragmentSearchBottomSheet;
import com.example.cryptocoin.fragments.FragmentWatchListBotomSheet;
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesItem;
import com.example.cryptocoin.retrofit.RetrofitQuotesWatchList;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchList extends AppCompatActivity {

    private ArrayList<String> arrayIdCVWatchList;
    private QuotesCryptoValute quotesCryptoValute;
    private Metadata metadata;

    private WatchListCV adapterWatchList;
    private WatchListCVEmpty adapterWatchListCVEmpty;

    private SharedPreferences mSettings;
    private Subscription subscription;

    private Toolbar toolbar;
    private Button btnAddCvWatchlist;
    private ListView listViewWatch;
    private TextView tvStatusWatchlist;

    ActivityResultLauncher<Intent> addWatchListStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        tvStatusWatchlist.setVisibility(View.GONE);
                        Intent intent = result.getData();
                        if (intent != null) {
                            arrayIdCVWatchList.add(intent.getStringExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE));
                            StringBuilder idStr = new StringBuilder();
                            for(int i = 0; i<arrayIdCVWatchList.size();i++){
                                if(i!=(arrayIdCVWatchList.size()-1)){
                                    idStr.append(arrayIdCVWatchList.get(i)).append(",");
                                }
                                else{
                                    idStr.append(arrayIdCVWatchList.get(i));
                                }
                            }
                            getQuotesWatchListCV(idStr.toString());
                        }
                    }
                }
            });

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar_watchlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Список отслеживания");

        tvStatusWatchlist = (TextView) findViewById(R.id.tv_status_watchlist);
        tvStatusWatchlist.setVisibility(View.GONE);

        mSettings = getSharedPreferences(Const.APP_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String strJson = mSettings.getString(Const.APP_PREFERENCES_ARRAY_ID_WATCHLIST, null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        arrayIdCVWatchList = gson.fromJson(strJson, type);

        listViewWatch = (ListView) findViewById(R.id.lv_watchlist);

        if (arrayIdCVWatchList == null){
            arrayIdCVWatchList = new ArrayList<>();
            tvStatusWatchlist.setVisibility(View.VISIBLE);
        }
        else {
            adapterWatchListCVEmpty = new WatchListCVEmpty(arrayIdCVWatchList.size());
            listViewWatch.setAdapter(adapterWatchListCVEmpty);
            StringBuilder idStr = new StringBuilder();
            for(int i = 0; i<arrayIdCVWatchList.size();i++){
                if(i!=(arrayIdCVWatchList.size()-1)){
                    idStr.append(arrayIdCVWatchList.get(i)).append(",");
                }
                else{
                    idStr.append(arrayIdCVWatchList.get(i));
                }
            }
            getQuotesWatchListCV(idStr.toString());
        }

        btnAddCvWatchlist = (Button) findViewById(R.id.btn_add_cv_watchlist);
        btnAddCvWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WatchList.this, AddWatchList.class);
                intent.putStringArrayListExtra(Const.ARRAY_ID_MESSAGE, arrayIdCVWatchList);
                addWatchListStartForResult.launch(intent);
            }
        });

        listViewWatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String id = arrayIdCVWatchList.get(i);
                QuotesItem quotesItem = quotesCryptoValute.getData().get(id);
                Item metadataItem = metadata.getData().get(id);
                FragmentWatchListBotomSheet fragmentWatchListBottomSheet = new FragmentWatchListBotomSheet(quotesItem,metadataItem);
                fragmentWatchListBottomSheet.show(getSupportFragmentManager(), fragmentWatchListBottomSheet.getTag());
            }
        });
    }

    private void getQuotesWatchListCV(String id) {
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = RetrofitQuotesWatchList.getQuotesWatchListCVObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(btnAddCvWatchlist, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(Map<String, Object> _quotesCVMetadata) {
                        quotesCryptoValute = (QuotesCryptoValute) _quotesCVMetadata.get(Const.CRYPTOVALUTE_KEY_MAP);
                        metadata = (Metadata) _quotesCVMetadata.get(Const.METADATA_KEY_MAP);
                        adapterWatchList = new WatchListCV(quotesCryptoValute, metadata, arrayIdCVWatchList);
                        listViewWatch.setAdapter(adapterWatchList);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(arrayIdCVWatchList.size() != 0) {
            SharedPreferences.Editor editor = mSettings.edit();
            Gson gson = new Gson();
            String jsonStr = gson.toJson(arrayIdCVWatchList);
            editor.putString(Const.APP_PREFERENCES_ARRAY_ID_WATCHLIST, jsonStr);
            editor.apply();
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
