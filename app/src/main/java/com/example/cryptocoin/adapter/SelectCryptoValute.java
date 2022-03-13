package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.cryptovalutepojo.CryptoValute;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.squareup.picasso.Picasso;

public class SelectCryptoValute extends BaseAdapter {

    private CryptoValute cryptoValuteList;
    private Metadata metadata;

    public SelectCryptoValute(CryptoValute _cryptoValuteList, Metadata _metadata){
        cryptoValuteList = _cryptoValuteList;
        metadata = _metadata;
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
            view = inflater.inflate(R.layout.tv_select_cv_list, viewGroup, false);
        }

        ImageView imageViewCryptoValuteLogo = (ImageView) view.findViewById(R.id.image_logo_cryptovalute_list_forconvert);
        TextView textViewNameCryptoValute = (TextView) view.findViewById(R.id.name_cryptovalute_list_forconvert);
        TextView textViewSymbolCryptoValute = (TextView) view.findViewById(R.id.symbol_cryptovalute_list_forconvert);

        String idCryptoValute = String.valueOf(cryptoValuteList.getData().get(i).getId());

        Picasso.get().load(metadata.getData().get(idCryptoValute).getLogo()).into(imageViewCryptoValuteLogo);
        textViewNameCryptoValute.setText(cryptoValuteList.getData().get(i).getName());
        textViewSymbolCryptoValute.setText(cryptoValuteList.getData().get(i).getSymbol());

        return view;
    }
}
