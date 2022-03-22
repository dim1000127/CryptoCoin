package com.example.cryptocoin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptocoin.R;
import com.example.cryptocoin.pojo.cryptovalutepojo.DataItem;
import com.example.cryptocoin.pojo.metadatapojo.Metadata;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GrowthFallRecyclerView extends RecyclerView.Adapter<GrowthFallRecyclerView.ViewHolder> {

    private Context context;
    private List<DataItem> cryptoValute;
    private Metadata metadata;

    public GrowthFallRecyclerView(List<DataItem> cryptoValute, Metadata metadata){
        this.cryptoValute = cryptoValute;
        this.metadata = metadata;
    }

    @NonNull
    @Override
    public GrowthFallRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthFallRecyclerView.ViewHolder holder, int position) {
        String idCrypto = String.valueOf(cryptoValute.get(position).getId());
        String cryptoSymbol = String.valueOf(cryptoValute.get(position).getSymbol());
        double cryptoPrice = cryptoValute.get(position).getQuote().getUsdDataCoin().getPrice();
        double cryptoPercentChange = cryptoValute.get(position).getQuote().getUsdDataCoin().getPercentChange24h();

        Picasso.get()
                .load(metadata.getData().get(idCrypto).getLogo())
                .into(holder.imageViewCrypto);
        holder.textViewSymbolCrypto.setText(cryptoSymbol);

        if(cryptoPrice<=1){
            holder.textViewPrice.setText(String.format("$%,.6f",cryptoPrice));
        }
        else {
            holder.textViewPrice.setText(String.format("$%,.2f", cryptoPrice));
        }

        holder.textViewChange24hCrypto.setText(String.format("%.2f%%", cryptoPercentChange));
        if(cryptoPercentChange >=0){ holder.textViewChange24hCrypto.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.green, null)); }
        else {holder.textViewChange24hCrypto.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.red, null)); }

    }

    @Override
    public int getItemCount() {
        return cryptoValute.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCrypto;
        TextView textViewSymbolCrypto;
        TextView textViewPrice;
        TextView textViewChange24hCrypto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewCrypto = itemView.findViewById(R.id.rv_imageCryptovalute);
            textViewSymbolCrypto = itemView.findViewById(R.id.rv_symbolCrypto);
            textViewPrice= itemView.findViewById(R.id.rv_priceCryptoValute);
            textViewChange24hCrypto = itemView.findViewById(R.id.rv_percent_change_24hCrypto);
        }
    }
}
