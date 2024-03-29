package com.example.cryptocoin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.CryptoValuteList;
import com.example.cryptocoin.adapter.WatchListCV;
import com.example.cryptocoin.adapter.WatchListCVEmpty;
import com.example.cryptocoin.fragments.FragmentBottomSheet;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.example.cryptocoin.pojo.metadatapojo.Item;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.example.cryptocoin.pojo.quotescryptovalute.QuotesCryptoValute;
import com.example.cryptocoin.retrofit.RetrofitQuotesWatchList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<String> arrayIdCVWatchList;
    private ArrayList<String> arrayIdItemForDelete;
    private QuotesCryptoValute quotesCryptoValute;
    private Metadata metadata;

    private boolean isSelectMode = false;

    private WatchListCV adapterWatchList;
    private WatchListCVEmpty adapterWatchListCVEmpty;

    private SharedPreferences mSettings;
    private Subscription subscription;

    private MenuItem menuItemDelete;
    private Toolbar toolbar;
    private Button btnAddCvWatchlist;
    private ListView listViewWatch;
    private SwipeRefreshLayout swipeRefreshLayout;
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

        arrayIdItemForDelete = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar_watchlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Список отслеживания");

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutWatchList);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvStatusWatchlist = findViewById(R.id.tv_status_watchlist);
        tvStatusWatchlist.setVisibility(View.GONE);

        mSettings = getSharedPreferences(Const.APP_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String strJson = mSettings.getString(Const.APP_PREFERENCES_ARRAY_ID_WATCHLIST, null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        arrayIdCVWatchList = gson.fromJson(strJson, type);

        listViewWatch = findViewById(R.id.lv_watchlist);

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

        btnAddCvWatchlist = findViewById(R.id.btn_add_cv_watchlist);
        btnAddCvWatchlist.setOnClickListener(view -> {
            Intent intent = new Intent(WatchList.this, AddWatchList.class);
            intent.putStringArrayListExtra(Const.ARRAY_ID_MESSAGE, arrayIdCVWatchList);
            addWatchListStartForResult.launch(intent);
        });

        listViewWatch.setOnItemClickListener((adapterView, view, i, l) -> {
            if(isSelectMode) {
                adapterWatchList.switchSelection(i);
                if(adapterWatchList.getValueSelection(i)){
                    arrayIdItemForDelete.add(arrayIdCVWatchList.get(i));
                }
                else{
                    arrayIdItemForDelete.remove(arrayIdCVWatchList.get(i));
                }
            }
            else{
                String id = arrayIdCVWatchList.get(i);
                DataItem dataItem = quotesCryptoValute.getData().get(id);
                Item metadataItem = metadata.getData().get(id);
                FragmentBottomSheet fragmentWatchListBottomSheet = new FragmentBottomSheet(dataItem, metadataItem);
                fragmentWatchListBottomSheet.show(getSupportFragmentManager(), fragmentWatchListBottomSheet.getTag());
            }
            if(adapterWatchList.getCountSelection() == 0){
                isSelectMode = false;
                menuItemDelete.setVisible(false);
            }
        });

        listViewWatch.setOnItemLongClickListener((adapterView, view, i, l) -> {
            isSelectMode = true;
            menuItemDelete.setVisible(true);
            adapterWatchList.switchSelection(i);
            if(adapterWatchList.getValueSelection(i)){
                arrayIdItemForDelete.add(arrayIdCVWatchList.get(i));
            }
            else{
                arrayIdItemForDelete.remove(arrayIdCVWatchList.get(i));
            }
            if(adapterWatchList.getCountSelection() == 0){
                isSelectMode = false;
                menuItemDelete.setVisible(false);
            }
            return true;
        });

        listViewWatch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (listViewWatch.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(listViewWatch.getFirstVisiblePosition() == 0 && listViewWatch.getChildAt(0).getTop() == 0);
                }
            }
        });
    }

    /*Map<String, DataItem> sortedMap = quotesCryptoValute.getData().entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue(Comparator.comparing(DataItem::getCmcRank))))
            .collect(Collectors
                    .toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));*/

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
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onRefresh() {
        StringBuilder idStr = new StringBuilder();
        for(int i = 0; i<arrayIdCVWatchList.size();i++){
            if(i!=(arrayIdCVWatchList.size()-1)){
                idStr.append(arrayIdCVWatchList.get(i)).append(",");
            }
            else{
                idStr.append(arrayIdCVWatchList.get(i));
            }
        }
        RetrofitQuotesWatchList.resetQuotesWatchListCVObservable(idStr.toString());
        getQuotesWatchListCV(idStr.toString());
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
        else{
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(Const.APP_PREFERENCES_ARRAY_ID_WATCHLIST, null);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_watchlist, menu);
        menuItemDelete = menu.findItem(R.id.delete_item_watchlist);
        menuItemDelete.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.delete_item_watchlist:
                deleteItemsWatchList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItemsWatchList() {
        for(int i = 0; i < arrayIdItemForDelete.size(); i++){
            arrayIdCVWatchList.remove(arrayIdItemForDelete.get(i));
            adapterWatchList.deleteItem(arrayIdItemForDelete.get(i));
        }
        adapterWatchList.notifyDataSetChanged();
        menuItemDelete.setVisible(false);
        if(adapterWatchList.getCount() == 0){
            tvStatusWatchlist.setVisibility(View.VISIBLE);
        }
    }
}
