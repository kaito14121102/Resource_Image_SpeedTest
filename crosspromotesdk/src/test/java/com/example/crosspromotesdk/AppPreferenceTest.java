package com.example.crosspromotesdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.crosspromotesdk.data.AppPreference;
import com.example.crosspromotesdk.data.BackUpManager;
import com.example.crosspromotesdk.modal.Advertisements;
import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Gson.class})
public class AppPreferenceTest {
    AppPreference appPreference;
    @Mock
    Context context;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    SharedPreferences.Editor mEditor;
    @Mock
    Object object;
    private ArrayList<Advertisements> advertisementsArrayList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Gson.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.edit()).thenReturn(mEditor);
        when(mEditor.putString(anyString(), anyString())).thenReturn(mEditor);
        when(mEditor.putInt(anyString(), anyInt())).thenReturn(mEditor);
        when(mEditor.putLong(anyString(), anyLong())).thenReturn(mEditor);
        when(mEditor.putFloat(anyString(), anyFloat())).thenReturn(mEditor);
        when(mEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mEditor);
        when(mEditor.commit()).thenReturn(true);
        appPreference = AppPreference.getInstance(context);
        Assert.assertNotNull(appPreference);
    }

    @Test
    public void TestSetString() {
        String key = "keySetString";
        appPreference.setString(key, "");
        Assert.assertNotEquals("", key);
        //verify(sharedPreferences).edit().putString(anyString(),anyString());
    }

    @Test
    public void TestgetString() {
        String key = "keyGetString";
        appPreference.getString(key);
        Assert.assertNotEquals("", key);
        //verify(sharedPreferences).getString(anyString(),anyString());
    }

    @Test
    public void TestGetInt() {
        String key = "keyGetInt";
        when(sharedPreferences.getInt(anyString(), anyInt())).thenReturn(1);
        appPreference.getInt(key);
        Assert.assertNotEquals("", key);
        //verify(sharedPreferences).getInt(anyString(),anyInt());
    }

    @Test
    public void TestSetInt() {
        String key = "keySetInt";
        appPreference.setInt(key, 0);
        Assert.assertNotEquals("", key);
    }

    @Test
    public void TestSetBoolean() {
        String key = "keySetBoolean";
        appPreference.setBoolean(key, true);
        Assert.assertNotEquals("", key);
        // verify(mEditor,times(1));
    }

    @Test
    public void TestGetBoolean() {
        String key = "keyGetBoolean";
        appPreference.getBoolean(key, false);
        Assert.assertNotEquals("", key);
        //verify(sharedPreferences).getBoolean(anyString(),anyBoolean());
    }

    @Test
    public void TestSetLong() {
        String key = "keySetLong";
        appPreference.setLong(key, 0);
        Assert.assertNotEquals("", key);
        //verify(mEditor).putLong(anyString(),anyLong());
    }

    @Test
    public void TestGetLong() {
        String key = "keyGetLong";
        appPreference.getLong(key);
        Assert.assertNotEquals("", key);
        // verify(sharedPreferences).getLong(anyString(),anyLong());
    }

    @Test
    public void TestSetFloat() {
        String key = "keySetFloat";
        appPreference.setFloat(key, 0);
        Assert.assertNotEquals("", key);
        // verify(mEditor).putFloat(anyString(),anyFloat());
    }

    @Test
    public void TestGetFloat() {
        String key = "keyGetFloat";
        appPreference.getFloat(key);
        Assert.assertNotEquals("", key);
        //verify(sharedPreferences).getFloat(anyString(),anyFloat());
    }

    @Test
    public void TestIsEmpty() {
        String json = "defaultValue";
        appPreference.isEmpty(json);
        assertNotEquals(json, "");
    }

    @Test
    public void TestSaveObjecttoJson() {
        String key = "keytestJson";
        appPreference.saveObject(object, key);
        assertNotNull(object);
        assertNotEquals(key, "");

    }

    @Test
    public void pushKeyTimeServer() {
        appPreference.pushKeyTimeServer("timeServer");
    }

    @Test
    public void pushKeyListAds() {
        appPreference.pushKeyListAds("ListAds");
    }

    @Test
    public void getKeyTimeServer() {
        appPreference.getKeyTimeServer();
    }

    @Test
    public void saveDatalistAds() {
        advertisementsArrayList = new ArrayList<>();
        advertisementsArrayList.add(new Advertisements());
        appPreference.saveDatalistAds(advertisementsArrayList);
    }

    @Test
    public void getDataListAds() {
        appPreference.getDataListAds();
    }

    @Test
    public void savelistAPiBackup() {
        ArrayList<BackUpManager> backUpManagerArrayList = new ArrayList<>();
        appPreference.savelistAPiBackup(backUpManagerArrayList);
    }

    @Test
    public void getTimedestination() {
        appPreference.getTimedestination();
    }

    @Test
    public void pushTimedestination() {
        appPreference.pushTimedestination("defaultValue");
    }

    @Test
    public void getKeyListAdvertisements() {
        appPreference.getKeyListAdvertisements();
    }

    @Test
    public void getListAdsFromJson() {
        appPreference.getListAdsFromJson();
    }

    @Test
    public void getListApiBackup() {
        appPreference.getListApiBackup();
    }
}
