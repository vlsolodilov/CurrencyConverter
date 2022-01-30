package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Currency> currencyList;
    ProgressDialog progressDialog;
    Button bRefresh;
    EditText etRubValue, etResult;
    TextInputLayout outlinedTextField2;

    private int nominal;
    private double value;
    private DataStorage dataStorage;

    private static final String BASE_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rvCurrency);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        outlinedTextField2 = (TextInputLayout) findViewById(R.id.outlinedTextField2);
        etRubValue = findViewById(R.id.etRubValue);
        etResult = findViewById(R.id.etResult);
        CurrencyAdapter adapter = new CurrencyAdapter(currencyList, new CurrencyAdapter.OnItemClickListener() {
            @Override public void onItemClick(Currency currency) {
                Log.d("Click", currency.getCharCode());
                outlinedTextField2.setHint(currency.getCharCode());
                nominal = currency.getNominal();
                value = currency.getValue();
                etResult.setText(convert(etRubValue.getText().toString()));
            }
        });
        recyclerView.setAdapter(adapter);
        dataStorage = new DataStorage(getSharedPreferences("pref", Context.MODE_PRIVATE));
        String date = dataStorage.loadDate();
        String data = dataStorage.loadData();
        if (data == null || !date.equals(DataStorage.FORMAT.format(Calendar.getInstance().getTime()))) {
            Log.d("loadPref", "date");
            new JsonTask().execute();
        } else {
            try {
                Log.d("loadPref", data);
                fillCurrencyList(new JSONObject(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        bRefresh = (Button) findViewById(R.id.bRefresh);
        bRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute();
            }
        });

        etRubValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etResult.setText(convert(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private class JsonTask extends AsyncTask<Void, Void, JSONObject> {

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected JSONObject doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(BASE_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                }
                dataStorage.saveData(buffer.toString());
                dataStorage.saveDate();
                return new JSONObject(buffer.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            currencyList.clear();
            fillCurrencyList(result);
        }
    }

    private String convert(String rubValue) {
        double rub;
        if (rubValue.length() != 0){
            rub = Double.parseDouble(rubValue);
        } else {
            rub = 0;
        }
        if (value != 0){
            return String.format("%.4f",rub / value * nominal);
        } else {
            return null;
        }
    }

    private void fillCurrencyList(JSONObject result) {
        try {
            JSONObject obj = result.getJSONObject("Valute");
            JSONObject obj2;
            Iterator<String> keys = obj.keys();
            Currency currency;
            while (keys.hasNext()) {
                String keyValue = (String) keys.next();
                obj2 = obj.getJSONObject(keyValue);
                currency = new Currency(obj2.getString("Name"),
                        obj2.getInt("Nominal"),
                        obj2.getString("CharCode"),
                        obj2.getDouble("Value"));
                currencyList.add(currency);
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("nominal", nominal);
        outState.putDouble("value", value);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nominal = savedInstanceState.getInt("nominal");
        value = savedInstanceState.getDouble("value");
    }
}