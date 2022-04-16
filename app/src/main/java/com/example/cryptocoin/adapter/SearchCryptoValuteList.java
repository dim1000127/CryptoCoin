package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.idcryptovalutepojo.IdCryptoValute;
import com.example.cryptocoin.pojo.idcryptovalutepojo.ItemID;

import java.util.ArrayList;
import java.util.List;

public class SearchCryptoValuteList extends BaseAdapter implements Filterable {

    private List<ItemID> idCryptoValuteList;
    private List<ItemID> idCryptoValuteListFiltered;

    public SearchCryptoValuteList (IdCryptoValute _idCryptoValuteList){
        idCryptoValuteList = _idCryptoValuteList.getData();
        idCryptoValuteListFiltered = _idCryptoValuteList.getData();
    }

    @Override
    public int getCount() {
        return idCryptoValuteListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
            view = inflater.inflate(R.layout.item_search_cv_list, viewGroup, false);
        }

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_list_search_cryptovalute);
        TextView textViewNameCryptoValute = (TextView) view.findViewById(R.id.name_cv_list_search);
        TextView textViewSymbolCryptoValute = (TextView) view.findViewById(R.id.symbol_cv_list_search);

        String idCryptoValuteItem = String.valueOf(idCryptoValuteListFiltered.get(i).getId());
        layout.setTag(idCryptoValuteItem);
        textViewNameCryptoValute.setText(idCryptoValuteListFiltered.get(i).getName());
        textViewSymbolCryptoValute.setText(idCryptoValuteListFiltered.get(i).getSymbol());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults filterResults = new FilterResults();

                if(charSequence == null || charSequence.length() == 0){
                    filterResults.count = idCryptoValuteList.size();
                    filterResults.values = idCryptoValuteList;
                }
                else{
                    String searchStr = charSequence.toString().toLowerCase();

                    List<ItemID> resultData = new ArrayList<>();
                    for (ItemID itemID:idCryptoValuteList){
                        if(itemID.getName().toLowerCase().startsWith(searchStr) || itemID.getSymbol().toLowerCase().startsWith(searchStr)){
                            resultData.add(itemID);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                idCryptoValuteListFiltered = (List<ItemID>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
