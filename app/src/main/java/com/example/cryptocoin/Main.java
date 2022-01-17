package com.example.cryptocoin;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main extends AppCompatActivity {

    BottomNavigationView bottomNav;
    Fragment selectedFragment;
    ActionBar actionBar;
    private static String apiKey = "908e2080-ad8d-4d43-bd0a-e65b8587d172";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        actionBar = getSupportActionBar();

        selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        bottomNav = findViewById(R.id.navigation_view);
        bottomNav.setOnItemSelectedListener(navListener);
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.navigation_home:
                        selectedFragment =  new HomeFragment();
                        //необходимо будет сделать свой ActionBar
                        actionBar.setTitle("CryptoCoin");
                        break;
                    case R.id.navigation_list_top:
                        selectedFragment =  new ListFragment();
                        actionBar.setTitle("Топ криптовалют");
                        break;
                    case R.id.navigation_bool_learn:
                        selectedFragment =  new BookLearnFragment();
                        actionBar.setTitle("Обучение");
                        break;
                }
                if(selectedFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
    };

    public void APICall() {
        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        /*List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("start","1"));
        paratmers.add(new BasicNameValuePair("limit","10"));
        paratmers.add(new BasicNameValuePair("convert","USD"));*/

        ContentValues paratmers = new ContentValues();
        paratmers.put("start", "1");
        paratmers.put("limit", "3");
        paratmers.put("convert", "USD");

        try {
            String result = makeAPICall(uri, paratmers);
            //System.out.println(result);
        } catch (IOException e) {
            System.out.println("Ошибка: нельзя получить доступ к содержимому - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Ошибка: неверный URL-адрес " + e.toString());
        }
    }

    public static String makeAPICall(String uri, ContentValues parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        /*URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }*/

        return response_content;
    }
}
