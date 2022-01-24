package com.example.cryptocoin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

public class AdapterSelectCryptoValute extends BaseAdapter {

    private CryptoValute cryptoValuteList;

    public AdapterSelectCryptoValute(CryptoValute _cryptoValuteList){
        cryptoValuteList = _cryptoValuteList;
    }

    @Override
    public int getCount() {
        return cryptoValuteList.getData().size();
    }

    @Override
    public Object getItem(int i) {
        return cryptoValuteList.getData().get(i);
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
            view = inflater.inflate(R.layout.tv_for_select_cryptovalute_list, viewGroup, false);
        }

        LinearLayout layout = (LinearLayout) view;
        TextView textViewNameCryptoValute = (TextView) layout.findViewById(R.id.name_cryptovalute_list_forconvert);
        TextView textViewSymbolCryptoValute = (TextView) layout.findViewById(R.id.symbol_cryptovalute_list_forconvert);
        layout.setTag(cryptoValuteList.getData().get(i).getId());
        textViewNameCryptoValute.setText(cryptoValuteList.getData().get(i).getName());
        textViewSymbolCryptoValute.setText(cryptoValuteList.getData().get(i).getSymbol());

        return view;
    }
}
