package com.example.cryptocoin.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.SearchCryptoValuteList;
import com.example.cryptocoin.adapter.SelectSearchCVListEmpty;
import com.example.cryptocoin.fragments.FragmentSearchBottomSheet;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.ItemID;
import com.example.cryptocoin.retrofit.RetrofitIdSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchCryptoValute extends AppCompatActivity {

    private Subscription subscription;
    private IdCryptoValute idCryptoValute;
    private SearchCryptoValuteList adapterIdCryptoValute;

    private Toolbar toolbar;
    private SearchView searchView;
    private TextView textViewStatuSearch;
    private ListView listViewSearchCV;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cv);
        toolbar = findViewById(R.id.toolbar_search_cv);
        setSupportActionBar(toolbar);

        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_search_layout);
        textViewStatuSearch = (TextView) findViewById(R.id.tv_status_search);
        listViewSearchCV = (ListView) findViewById(R.id.lv_search_cryptovalute);
        listViewSearchCV.setEmptyView(textViewStatuSearch);
        fillEmptySearchList();
        searchView = findViewById(R.id.searchview);
        textViewStatuSearch.setVisibility(View.GONE);

        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(idCryptoValute != null){
                    adapterIdCryptoValute.getFilter().filter(s);
                }
                return true;
            }
        });

        listViewSearchCV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout layout = view.findViewById(R.id.layout_list_search_cryptovalute);
                String id = layout.getTag().toString();
                /*Snackbar.make(constraintLayout, "Идентификатор токена: "+id,Snackbar.LENGTH_SHORT)
                        .show();*/
                FragmentSearchBottomSheet fragmentSearchBottomSheet = new FragmentSearchBottomSheet(id);
                fragmentSearchBottomSheet.show(getSupportFragmentManager(), fragmentSearchBottomSheet.getTag());
            }
        });

        getIdCryptoValute();
    }

    private void fillEmptySearchList(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        SelectSearchCVListEmpty adapterCVSearchListEmpty = new SelectSearchCVListEmpty(Math.round(dpHeight/60)+2);
        listViewSearchCV.setAdapter(adapterCVSearchListEmpty);
        listViewSearchCV.setEnabled(false);
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
                            Snackbar.make(constraintLayout, R.string.connection_error, Snackbar.LENGTH_LONG)
                                    .show();
                        }

                    @Override
                    public void onNext(IdCryptoValute _idCryptoValute) {
                        idCryptoValute = _idCryptoValute;
                        idCryptoValute.getData().sort(Comparator.comparing(ItemID::getName));
                        adapterIdCryptoValute = new SearchCryptoValuteList(idCryptoValute);
                        listViewSearchCV.setAdapter(adapterIdCryptoValute);
                        listViewSearchCV.setEnabled(true);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_cancel:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
