package com.gdt.speedtest;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;
import com.gdt.speedtest.data.manager.DataManager;
import com.gdt.speedtest.injection.component.AppComponent;
import com.gdt.speedtest.injection.component.DaggerAppComponent;
import com.gdt.speedtest.injection.module.AppModule;
import com.gdt.speedtest.util.AppPreference;
import com.gdt.speedtest.util.DatabaseUtil;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;


public class SpeedTestApplication extends MultiDexApplication {

    private AppComponent appComponent;
    public static SharedPreferences preferences;


    public static SpeedTestApplication get(Context context) {
        return (SpeedTestApplication) context.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        // Set up Crashlytics, disabled for debug builds
//        Crashlytics crashlyticsKit = new Crashlytics.Builder()
//                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                .build();
//        // Initialize Fabric with the debug-disabled crashlytics.
//        Fabric.with(this, crashlyticsKit, new Crashlytics());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        DatabaseUtil.checkAndMigrateDatabase(this);
        DataManager.getInstance().init(this);
//        MultiDex.install(this);
        AppPreference.getInstance(this);
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
    }

    public AppComponent getComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
