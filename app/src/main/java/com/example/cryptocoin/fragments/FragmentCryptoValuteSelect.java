package com.example.cryptocoin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.R;
import com.example.cryptocoin.adapter.SelectCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.ItemID;
import com.example.cryptocoin.retrofit.RetrofitIdSingleton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentCryptoValuteSelect extends Fragment {

    private Subscription subscription;

    private SearchView searchView;
    private ListView listViewSelectCryptoValute;
    private SelectCryptoValute adapterSelectCryptoValute;
    private TextView textViewStatusSearch;

    private IdCryptoValute idCryptoValute = null;

    public interface OnSelectCVListener{
        void onSelectCV(String id);
    }

    private OnSelectCVListener onSelectCVListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onSelectCVListener = (OnSelectCVListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement onSelectFiatListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_cryptovalute_select, container, false);

        searchView = (SearchView) getActivity().findViewById(R.id.searchview_select);
        textViewStatusSearch = (TextView) view.findViewById(R.id.tv_status_search_select);
        listViewSelectCryptoValute = (ListView) view.findViewById(R.id.listview_select_cryptovalute);
        listViewSelectCryptoValute.setEmptyView(textViewStatusSearch);
        textViewStatusSearch.setVisibility(View.GONE);

        listViewSelectCryptoValute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LinearLayout layout = view.findViewById(R.id.layout_list_select_cryptovalute);
                String id = layout.getTag().toString();
                onSelectCVListener.onSelectCV(id);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.background_searchview, null));
                searchView.setIconifiedByDefault(false);
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
                return true;
            }
        });
        getIdCryptoValute();
        return view;
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
                        Snackbar.make(listViewSelectCryptoValute, R.string.connection_error, Snackbar.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onNext(IdCryptoValute _idCryptoValute) {
                        idCryptoValute = _idCryptoValute;
                        idCryptoValute.getData().sort(Comparator.comparing(ItemID::getName));
                        adapterSelectCryptoValute = new SelectCryptoValute(idCryptoValute);
                        listViewSelectCryptoValute.setAdapter(adapterSelectCryptoValute);
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
}
