package com.example.crosspromotesdk.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.crosspromotesdk.modal.Advertisements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AppPreference {
    public final String KEY_TIME_SERVER = "KEY_TIME_SERVER";
    public final String KEY_LIST_ADVERTISEMENTS = "KEY_LIST_ADVERTISEMENTS";
    public final String KEY_NEW_LIST_ADS = "KEY_NEW_LIST_ADS";
    public final String KEY_BUILD_VERSION = "KEY_BUILD_VERSION";
    public final String KEY_TIME_DESTINATION = "KEY_TIME_DESTINATION";
    public final String KEY_LIST_API_BACKUP = "KEY_LIST_API_BACKUP";
    public final String KEY_HAVE_CACHE = "KEY_HAVE_CACHE";

    public Gson gson;

    public static AppPreference sInstance;
    public Context mContext;
    private SharedPreferences sharedPreferences;

    public static AppPreference getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPreference(context.getApplicationContext());
        }
        return sInstance;
    }

    private AppPreference(final Context context) {
        this.mContext = context;
        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveObject(Object obj, String key) {
        String json = "";
        if (gson != null) {
            json = gson.toJson(obj);
        }
        setString(key, json);
    }


    public boolean isEmpty(String json) {
        return json == null || json.trim().length() == 0;
    }

    public void pushKeyTimeServer(String timestamp) {
        setString(KEY_TIME_SERVER, timestamp);
    }

    public void pushKeyListAds(String listads) {
        setString(KEY_LIST_ADVERTISEMENTS, listads);
    }


    public String getKeyTimeServer() {
        return getString(KEY_TIME_SERVER);
    }

    public void saveDatalistAds(ArrayList<Advertisements> listads) {
        saveObject(listads, KEY_NEW_LIST_ADS);
    }

    public ArrayList<Advertisements> getDataListAds() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Advertisements>>() {
        }.getType();
        ArrayList<Advertisements> list = gson.fromJson(getString(KEY_NEW_LIST_ADS), listType);
        return list;
    }

    public ArrayList<Advertisements> getListAdsFromJson() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Advertisements>>() {
        }.getType();
        ArrayList<Advertisements> map = gson.fromJson(getKeyListAdvertisements(), listType);
        return map;
    }

    public String getKeyListAdvertisements() {
        return getString(KEY_LIST_ADVERTISEMENTS);
    }

    public void pushTimedestination(String message) {
        setString(KEY_TIME_DESTINATION, message);
    }

    public String getTimedestination() {
        return getString(KEY_TIME_DESTINATION);
    }

    public void pushBuildVersion(String message) {
        setString(KEY_BUILD_VERSION, message);
    }

    public String getBuildVersion() {
        return getString(KEY_BUILD_VERSION);
    }

    public void savelistAPiBackup(ArrayList<BackUpManager> listApiBackup) {
        saveObject(listApiBackup, KEY_LIST_API_BACKUP);
    }

    public ArrayList<BackUpManager> getListApiBackup() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<BackUpManager>>() {
        }.getType();
        ArrayList<BackUpManager> list = gson.fromJson(getString(KEY_LIST_API_BACKUP), listType);
        return list;
    }

    public void pushKeyLoadCache(boolean isLoad) {
        setBoolean(KEY_HAVE_CACHE, isLoad);
    }

    public boolean getKeyLoadCache() {
        return getBoolean(KEY_HAVE_CACHE, false);
    }

    public void clearSharePreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TIME_SERVER);
        editor.remove(KEY_LIST_ADVERTISEMENTS);
        editor.remove(KEY_NEW_LIST_ADS);
        editor.remove(KEY_TIME_DESTINATION);
        editor.remove(KEY_LIST_API_BACKUP);
        editor.apply();
    }
}
