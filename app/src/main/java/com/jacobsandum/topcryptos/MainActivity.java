package com.jacobsandum.topcryptos;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String TAG = "TopCryptos MainActivity";

    private static String url = "https://api.coinmarketcap.com/v1/ticker/?limit=10000000";

    private ArrayList<Crypto> cryptoList = new ArrayList<Crypto>();

    private Crypto[] top1HR = new Crypto[10];
    private Crypto[] top24HR = new Crypto[10];
    private Crypto[] top7D = new Crypto[10];

    private final int HR = 0;
    private final int D = 1;
    private final int D7 = 2;

    private boolean enabled = false;

    //UI
    Spinner dropdown;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dropdown = (Spinner) findViewById(R.id.spinner);
        textView = (TextView) findViewById(R.id.textView);
        String items[] = new String[]{"1 Hour", "1 Day", "7 Days"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setOnItemSelectedListener(this);
        dropdown.setAdapter(adapter);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));

        //Execute Async
        new getCryptos().execute();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (position) {
            case HR:
                if (enabled == true) {
                    //1 Hour selected
                    textView.setText("");
                    for (int i = 0; i < 10; i++) {
                        String positive = "";
                        if (top1HR[i].percentChange1HR >= 0) {

                            positive = "+";
                        }
                        textView.setTextColor(Color.RED);
                        if (top1HR[i].percentChange1HR > 0) {
                            textView.setTextColor(Color.GREEN);
                        }
                        if (top1HR[i].percentChange1HR == 0) {
                            textView.setTextColor(Color.BLACK);
                        }
                        textView.setText(textView.getText() + top1HR[i].name + ":" + "\n" + positive + top1HR[i].percentChange1HR + "%" + "\n");
                    }
                }
                break;
            case D:
                if (enabled == true) {
                    //1 Day selected
                    textView.setText("");
                    for (int i = 0; i < 10; i++) {
                        String positive = "";
                        if (top24HR[i].percentChange24HR >= 0) {
                            positive = "+";
                        }
                        textView.setTextColor(Color.RED);
                        if (top24HR[i].percentChange24HR > 0) {
                            textView.setTextColor(Color.GREEN);
                        }
                        if (top24HR[i].percentChange24HR == 0) {
                            textView.setTextColor(Color.BLACK);
                        }
                        //TODO: Change color, with shades decreasing from highest
                        textView.setText(textView.getText() + top24HR[i].name + ":" + "\n" + positive + top24HR[i].percentChange24HR + "%" + "\n");
                    }
                }
                break;
            case D7:
                if (enabled == true) {
                    //7 Days selected
                    textView.setText("");
                    for (int i = 0; i < 10; i++) {
                        String positive = "";
                        if (top7D[i].percentChange7D >= 0) {
                            positive = "+";
                        }
                        textView.setTextColor(Color.RED);
                        if (top7D[i].percentChange7D > 0) {
                            textView.setTextColor(Color.GREEN);
                        }
                        if (top7D[i].percentChange7D == 0) {
                            textView.setTextColor(Color.BLACK);
                        }
                        //TODO: Change color, with shades decreasing from highest
                        textView.setText(textView.getText() + top7D[i].name + ":" + "\n" + positive + top7D[i].percentChange7D + "%" + "\n");
                    }
                }
                break;

        }
    }

    /*
        Async task to fetch JSON data
     */
    private class getCryptos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Stuff before execution (Loading Indicator?)

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            enabled = false;
            HttpHandler httpHandler = new HttpHandler();

            //Make request through Http Handler
            String data = httpHandler.makeServiceCall(url);

            //Log.e(TAG, "Response from " + url + data);

            if (data != null) {
                try {
                    //Fetch JSON Array Node
                    JSONArray cryptos = new JSONArray(data);

                    //Loop through all cryptos
                    for (int i = 0; i < cryptos.length(); i++) {
                        JSONObject cryptoJSONObject = cryptos.getJSONObject(i);

                        String name = cryptoJSONObject.getString("name");
                        String symbol = cryptoJSONObject.getString("symbol");
                        double priceUSD = 0;
                        if (!(cryptoJSONObject.isNull("price_usd"))) {
                            priceUSD = Double.parseDouble(cryptoJSONObject.getString("price_usd"));
                        }
                        double marketCapUSD = 0;
                        if (!(cryptoJSONObject.isNull("market_cap_usd"))) {
                            marketCapUSD = Double.parseDouble(cryptoJSONObject.getString("market_cap_usd"));
                        }
                        double percentChange1HR = 0;
                        if (!(cryptoJSONObject.isNull("percent_change_1h"))) {
                            percentChange1HR = Double.parseDouble(cryptoJSONObject.getString("percent_change_1h"));
                        }
                        double percentChange24HR = 0;
                        if (!(cryptoJSONObject.isNull("percent_change_24h"))) {
                            percentChange24HR = Double.parseDouble(cryptoJSONObject.getString("percent_change_24h"));
                        }
                        double percentChange7D = 0;
                        if (!(cryptoJSONObject.isNull("percent_change_7d"))) {
                            percentChange7D = Double.parseDouble(cryptoJSONObject.getString("percent_change_7d"));
                        }


                        Crypto crypto = new Crypto(name, symbol, priceUSD, marketCapUSD,
                                percentChange1HR, percentChange24HR, percentChange7D);
                        //Log.e(TAG, crypto.toString());
                        cryptoList.add(crypto);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get JSON from server! (JSONException)",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get JSON from server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get JSON from server!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            enabled = true;

            //Turn off loading indicator here

            //Set data into UI here

            //1 Hour
            Crypto[] copy1 = new Crypto[cryptoList.size()];
            for (int l = 0; l < copy1.length; l++)
                copy1[l] = cryptoList.get(l);
            //Crypto largest1HR = new Crypto();
            Crypto largest1HR;
            int jIndex = 0;
            for (int i = 0; i < 10; i++) {
                largest1HR = copy1[0];
                for (int j = 0; j < copy1.length; j++) {
                    if (copy1[j].percentChange1HR > largest1HR.percentChange1HR) {
                        largest1HR = copy1[j];
                        jIndex = j;
                    }
                    //top1HR[i] = largest1HR;
                }
                top1HR[i] = largest1HR;
                copy1[jIndex] = new Crypto();
            }
            if (dropdown.getSelectedItemPosition() == HR) {
                for (int i = 0; i < 10; i++) {
                    String positive = "";
                    if (top1HR[i].percentChange1HR >= 0) {

                        positive = "+";
                    }
                    textView.setTextColor(Color.RED);
                    if (top1HR[i].percentChange1HR > 0) {
                        textView.setTextColor(Color.GREEN);
                    }
                    if (top1HR[i].percentChange1HR == 0) {
                        textView.setTextColor(Color.BLACK);
                    }
                    textView.setText(textView.getText() + top1HR[i].name + ":" + "\n" + positive + top1HR[i].percentChange1HR + "%" + "\n");
                }
            }
            //top1HRTextView.setText(top1HR.toString());

            //24 Hours
            Crypto[] copy2 = new Crypto[cryptoList.size()];
            for (int l = 0; l < copy2.length; l++)
                copy2[l] = cryptoList.get(l);
            //Crypto largest1HR = new Crypto();
            Crypto largest24HR;
            jIndex = 0;
            for (int i = 0; i < 10; i++) {
                largest24HR = copy2[0];
                for (int j = 0; j < copy2.length; j++) {
                    if (copy2[j].percentChange24HR > largest24HR.percentChange24HR) {
                        largest24HR = copy2[j];
                        jIndex = j;
                    }
                    //top1HR[i] = largest1HR;
                }
                top24HR[i] = largest24HR;
                copy2[jIndex] = new Crypto();
            }
            if (dropdown.getSelectedItemPosition() == D) {
            for (int i = 0; i < 10; i++) {
                    String positive = "";
                    if (top24HR[i].percentChange24HR >= 0) {
                        positive = "+";
                    }
                    textView.setTextColor(Color.RED);
                    if (top24HR[i].percentChange24HR > 0) {
                        textView.setTextColor(Color.GREEN);
                    }
                    if (top24HR[i].percentChange24HR == 0) {
                        textView.setTextColor(Color.BLACK);
                    }
                    //TODO: Change color, with shades decreasing from highest
                    textView.setText(textView.getText() + top24HR[i].name + ":" + "\n" + positive + top24HR[i].percentChange24HR + "%" + "\n");
                }
            }

            //7 Days
            Crypto[] copy3 = new Crypto[cryptoList.size()];
            for (int l = 0; l < copy3.length; l++)
                copy3[l] = cryptoList.get(l);
            Crypto largest7D;
            jIndex = 0;
            for (int i = 0; i < 10; i++) {
                largest7D = copy3[0];
                for (int j = 0; j < copy3.length; j++) {
                    if (copy3[j].percentChange7D > largest7D.percentChange7D) {
                        largest7D = copy3[j];
                        jIndex = j;
                    }
                    //top1HR[i] = largest1HR;
                }
                top7D[i] = largest7D;
                copy3[jIndex] = new Crypto();
            }
            if (dropdown.getSelectedItemPosition() == D7) {
            for (int i = 0; i < 10; i++) {
                    String positive = "";
                    if (top7D[i].percentChange7D >= 0) {
                        positive = "+";
                    }
                    textView.setTextColor(Color.RED);
                    if (top7D[i].percentChange7D > 0) {
                        textView.setTextColor(Color.GREEN);
                    }
                    if (top7D[i].percentChange7D == 0) {
                        textView.setTextColor(Color.BLACK);
                    }
                    //TODO: Change color, with shades decreasing from highest
                    textView.setText(textView.getText() + top7D[i].name + ":" + "\n" + positive + top7D[i].percentChange7D + "%" + "\n");
                }
            }
        }
    }

}
