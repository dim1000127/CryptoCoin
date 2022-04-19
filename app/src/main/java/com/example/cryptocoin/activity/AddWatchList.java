package com.example.cryptocoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cryptocoin.Const;
import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.AddWatchListCV;
import com.example.cryptocoin.adapter.SelectSearchCVListEmpty;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.ItemID;
import com.example.cryptocoin.retrofit.RetrofitIdSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddWatchList extends AppCompatActivity {

    private Subscription subscription;
    private AddWatchListCV adapterAddWatchListCV;

    private IdCryptoValute idCryptoValute;
    private ArrayList<String> arrayIdWatchList;

    private Toolbar toolbar;
    private ListView listViewSelectWatchList;
    private SearchView searchView;
    private TextView tvStatusSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cv_watchlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_watchlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Выберите актив");

        Intent intent = getIntent();
        arrayIdWatchList = intent.getStringArrayListExtra(Const.ARRAY_ID_MESSAGE);

        tvStatusSearch = (TextView) findViewById(R.id.tv_status_search_add_watchlist);
        searchView = (SearchView) findViewById(R.id.searchview_add_watchlist);
        listViewSelectWatchList = (ListView) findViewById(R.id.lv_add_watchlist);
        listViewSelectWatchList.setEmptyView(tvStatusSearch);
        fillEmptySelectWatchList();

        listViewSelectWatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConstraintLayout layout = view.findViewById(R.id.layout_add_watchlist_cryptovalute);
                String id = layout.getTag().toString();

                if(arrayIdWatchList.contains(id)){
                    Snackbar.make(toolbar, "Этот актив уже находится в списке отслеживания", Snackbar.LENGTH_SHORT)
                            .show();
                }
                else{
                    Intent data = new Intent();
                    data.putExtra(Const.ID_CRYPTOVALUTE_ITEM_MESSAGE, id);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(idCryptoValute != null){
                    adapterAddWatchListCV.getFilter().filter(s);
                }
                return true;
            }
        });

        tvStatusSearch.setVisibility(View.GONE);

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
                        Snackbar.make(toolbar, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(IdCryptoValute _idCryptoValute) {
                        idCryptoValute = _idCryptoValute;
                        idCryptoValute.getData().sort(Comparator.comparing(ItemID::getName));
                        adapterAddWatchListCV = new AddWatchListCV(idCryptoValute, arrayIdWatchList);
                        listViewSelectWatchList.setAdapter(adapterAddWatchListCV);
                        listViewSelectWatchList.setEnabled(true);
                    }
                });
    }

    private void fillEmptySelectWatchList() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        SelectSearchCVListEmpty adapterCVSearchListEmpty = new SelectSearchCVListEmpty(Math.round(dpHeight/60)+2);
        listViewSelectWatchList.setAdapter(adapterCVSearchListEmpty);
        listViewSelectWatchList.setEnabled(false);
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
