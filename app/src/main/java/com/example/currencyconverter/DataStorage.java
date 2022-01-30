package com.example.currencyconverter;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataStorage {
    private final SharedPreferences preferences;
    private static final String DATE = "Date";
    private static final String DATA = "Data";
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());

    public DataStorage(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveDate() {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(DATE, FORMAT.format(Calendar.getInstance().getTime()));
        ed.apply();
    }

    public void saveData(String data) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(DATA, data);
        ed.apply();
    }

    public String loadDate() {
        return preferences.getString(DATE, null);
    }

    public String loadData() {
        return preferences.getString(DATA, null);
    }
}
