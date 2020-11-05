package com.example.crosspromotesdk;

import android.content.Context;
import androidx.annotation.CallSuper;

import com.example.crosspromotesdk.network.Networkmodule;
import com.readystatesoftware.chuck.ChuckInterceptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Retrofit.class, ChuckInterceptor.class, HttpLoggingInterceptor.class})
@PowerMockIgnore("javax.net.ssl.*")
public class NetWorkModuleTest {
    Networkmodule networkmodule;
    @Mock
    Context context;
    private String packageName;

    @Before
    @CallSuper
    public void setup() {
        MockitoAnnotations.initMocks(this);
        packageName = "https://crossdev.ecomobile.vn/";
        networkmodule = new Networkmodule(context, packageName);
        assertNotEquals("", packageName);
    }

    @Test
    public void TestprovideRetrofit() {
        when(context.getApplicationContext()).thenReturn(context);
        networkmodule.provideRetrofit();
        assertNotNull(context);
    }

    @Test
    public void TestprovideChuck() {
        when(context.getApplicationContext()).thenReturn(context);
        networkmodule.provideChuckInterceptor();
    }

    @Test
    public void TestprovideStetho() {
        when(context.getApplicationContext()).thenReturn(context);
        networkmodule.provideStethoInterceptor();
    }

    @Test
    public void TestprovideHttpLogging() {
        when(context.getApplicationContext()).thenReturn(context);
        networkmodule.provideHttpLoggingInterceptor();
    }

    @Test
    public void provideGson() {
        networkmodule.provideGson();
    }
}
