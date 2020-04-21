package com.example.recviewfragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.recviewfragment.Model.ItemHost;
import com.google.gson.Gson;

public class PreferenceUtils {

    // shared pref mode
    private final int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "PREF_NAME";
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferenceUtils(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setBoolean(String key, boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return pref.getString(key, "");
    }

    public void setInteger(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getInteger(String key) {
        return pref.getInt(key, 0);
    }

    public void clearSavedInSharedPreference(){
        editor.clear();
        editor.commit();
    }

    //serialize itemHost into JSON
    public String serializeToJson(ItemHost itemHost) {
        Gson gson = new Gson();
        String j = gson.toJson(itemHost);
        return j;
    }

    //deserialize JSON into ItemHost
    public ItemHost deserializeFromJson(String jsonString) {
        Gson gson = new Gson();
        ItemHost itemHost = gson.fromJson(jsonString, ItemHost.class);
        return itemHost;
    }
}
