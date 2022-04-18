package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.cryptocoin.R;

public class SelectSearchCVListEmpty extends BaseAdapter {

    private int count;

    public SelectSearchCVListEmpty (int count){
        this.count = count;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_empty_searchselect_cv_list, viewGroup, false);
        }

        return view;
    }
}
