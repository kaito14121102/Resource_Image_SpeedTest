package com.example.crosspromotesdk;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.example.crosspromotesdk.data.AppUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppUtilTest {
    public final int MDPI = 160;
    public final int HDPI = 240;
    public final int XHDPI = 320;
    public final int XXHDPI = 480;
    public final int XXXHDPI = 640;
    AppUtil appUtil;
    @Mock
    Context context;
    @Mock
    NetworkInfo networkInfo;
    @Mock
    NetworkInfo networkInfo_3G;
    @Mock
    NetworkInfo networkInfo_WIFI;
    @Mock
    ConnectivityManager connectivityManager;
    @Mock
    Resources resources;
    @Mock
    DisplayMetrics displayMetrics;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        appUtil = new AppUtil(context);
        assertNotNull(context);
        assertNotNull(appUtil);
    }

    @Test
    public void TestIsOnline() {
        Mockito.reset(connectivityManager);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        appUtil.isOnline();
        verify(connectivityManager, times(1)).getActiveNetworkInfo();
        verify(networkInfo).isConnectedOrConnecting();

        assertNotNull(connectivityManager);
        assertNotNull(context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Test
    public void TestCheckTypeNetWork3G() {
        Mockito.reset(connectivityManager);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo_3G);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo_WIFI);
        when(networkInfo_3G.isConnectedOrConnecting()).thenReturn(true);
        appUtil.checkTypeNetwork();
        verify(connectivityManager, times(1)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assertNotNull(context);
        assertNotNull(connectivityManager);
        assertNotNull(context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Test
    public void TestCheckTypeNetWorkWIFI() {
        Mockito.reset(connectivityManager);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo_3G);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo_WIFI);
        when(networkInfo_WIFI.isConnectedOrConnecting()).thenReturn(true);
        appUtil.checkTypeNetwork();
        verify(connectivityManager, times(1)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assertNotNull(context);
        assertNotNull(connectivityManager);
        assertNotNull(context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Test
    public void TestCheckTypeNetWorkNONE() {
        Mockito.reset(connectivityManager);
        when(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).thenReturn(networkInfo_3G);
        when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo_WIFI);
        when(networkInfo_WIFI.isConnectedOrConnecting()).thenReturn(false);
        when(networkInfo_3G.isConnectedOrConnecting()).thenReturn(false);
        appUtil.checkTypeNetwork();
        verify(connectivityManager, times(1)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assertNotNull(context);
        assertNotNull(connectivityManager);
        assertNotNull(context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @Test
    public void TestgetDeviceResolutionMDPI() {
        Mockito.reset(context);
        Mockito.reset(resources);
        Mockito.reset(displayMetrics);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        appUtil.setDensity(MDPI);
        appUtil.getDeviceResolution();
        verify(context, times(2)).getResources();
    }

    @Test
    public void TestgetDeviceResolutionHDPI() {
        Mockito.reset(context);
        Mockito.reset(resources);
        Mockito.reset(displayMetrics);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        appUtil.setDensity(HDPI);
        appUtil.getDeviceResolution();
        verify(context, times(2)).getResources();
    }

    @Test
    public void TestgetDeviceResolutionXHDPI() {
        Mockito.reset(context);
        Mockito.reset(resources);
        Mockito.reset(displayMetrics);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        appUtil.setDensity(XHDPI);
        appUtil.getDeviceResolution();
        verify(context, times(2)).getResources();
    }

    @Test
    public void TestgetDeviceResolutionXXHDPI() {
        Mockito.reset(context);
        Mockito.reset(resources);
        Mockito.reset(displayMetrics);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        appUtil.setDensity(XXHDPI);
        appUtil.getDeviceResolution();
        verify(context, times(2)).getResources();
    }

    @Test
    public void TestgetDeviceResolutionXXXHDPI() {
        Mockito.reset(context);
        Mockito.reset(resources);
        Mockito.reset(displayMetrics);
        when(context.getResources()).thenReturn(resources);
        when(resources.getDisplayMetrics()).thenReturn(displayMetrics);
        appUtil.setDensity(XXXHDPI);
        appUtil.getDeviceResolution();
        verify(context, times(2)).getResources();
    }
}
