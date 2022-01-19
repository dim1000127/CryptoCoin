package com.example.cryptocoin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cryptocoin.cryptovalutepojo.CryptoValute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    View rootView;
    ImageView imageViewCryptoTop1;
    ImageView imageViewCryptoTop2;
    ImageView imageViewCryptoTop3;
    TextView textViewSymbolCryptoOne;
    TextView textViewSymbolCryptoTwo;
    TextView textViewSymbolCryptoThree;
    TextView textViewChange24hCryptoOne;
    TextView textViewChange24hCryptoTwo;
    TextView textViewChange24hCryptoThree;
    TextView textViewPriceCryptoOne;
    TextView textViewPriceCryptoTwo;
    TextView textViewPriceCryptoThree;

    //CryptoValute dataCryptoValute = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        imageViewCryptoTop1 = rootView.findViewById(R.id.imageCryptovaluteOne);
        imageViewCryptoTop2 = rootView.findViewById(R.id.imageCryptovaluteTwo);
        imageViewCryptoTop3 = rootView.findViewById(R.id.imageCryptovaluteThree);
        textViewSymbolCryptoOne = rootView.findViewById(R.id.symbolCryptoOne);
        textViewSymbolCryptoTwo = rootView.findViewById(R.id.symbolCryptoTwo);
        textViewSymbolCryptoThree = rootView.findViewById(R.id.symbolCryptoThree);
        textViewChange24hCryptoOne =rootView.findViewById(R.id.percent_change_24hCryptoOne);
        textViewChange24hCryptoTwo =rootView.findViewById(R.id.percent_change_24hCryptoTwo);
        textViewChange24hCryptoThree =rootView.findViewById(R.id.percent_change_24hCryptoThree);
        textViewPriceCryptoOne = rootView.findViewById(R.id.priceCryptoValuteOne);
        textViewPriceCryptoTwo = rootView.findViewById(R.id.priceCryptoValuteTwo);
        textViewPriceCryptoThree = rootView.findViewById(R.id.priceCryptoValuteThree);
        //fillBlocksTopThree();
        return  inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void fillBlocksTopThree(){
        if(Main.cryptoValute!=null) {
            textViewSymbolCryptoOne.setText(Main.cryptoValute.getData().get(0).getSymbol());
            textViewSymbolCryptoTwo.setText(Main.cryptoValute.getData().get(1).getSymbol());
            textViewSymbolCryptoThree.setText(Main.cryptoValute.getData().get(2).getSymbol());
            textViewChange24hCryptoOne.setText(String.format("%.2f", Main.cryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPercentChange24h()));
            textViewChange24hCryptoTwo.setText(String.format("%.2f", Main.cryptoValute.getData().get(1).getQuote().getUsdDataCoin().getPercentChange24h()));
            textViewChange24hCryptoThree.setText(String.format("%.2f", Main.cryptoValute.getData().get(2).getQuote().getUsdDataCoin().getPercentChange24h()));
            textViewPriceCryptoOne.setText(String.format("%.2f", Main.cryptoValute.getData().get(0).getQuote().getUsdDataCoin().getPrice()));
            textViewPriceCryptoTwo.setText(String.format("%.2f", Main.cryptoValute.getData().get(1).getQuote().getUsdDataCoin().getPrice()));
            textViewPriceCryptoThree.setText(String.format("%.2f", Main.cryptoValute.getData().get(2).getQuote().getUsdDataCoin().getPrice()));
        }
    }
}
