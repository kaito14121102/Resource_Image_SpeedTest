package com.gdt.speedtest.features.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.gdt.speedtest.data.manager.DataManager;
import com.gdt.speedtest.database.Result;
import com.gdt.speedtest.features.complete.CompleteActivity;
import com.gdt.speedtest.features.main.fragment.result.ResultsFragment;
import com.gdt.speedtest.features.main.fragment.settings.SettingFragment;
import com.gdt.speedtest.features.main.fragment.speed.SpeedFragmentUpdate;
import com.gdt.speedtest.util.AppPreference;
import com.gdt.speedtest.utilcross.UtilAdsCrossAdaptive;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.gdt.speedtest.Constants.EXTRA_KEY_ADS_FULL_CLICK_GO;
import static com.gdt.speedtest.Constants.EXTRA_KEY_ADS_RESULT;
import static com.gdt.speedtest.Constants.EXTRA_KEY_ADS_SETTINGS;
import static com.gdt.speedtest.Constants.STATUS_DOWNLOAD;
import static com.gdt.speedtest.Constants.STATUS_NOT_START;
import static com.gdt.speedtest.Constants.STATUS_UPLOAD;
import static com.gdt.speedtest.utilcross.ListAdsCross.getListCrossAdaptive;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PageListener {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.img_speed)
    ImageView imgSpeed;
    @BindView(R.id.img_results)
    ImageView imgResults;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.txt_test)
    TextView txtTest;
    @BindView(R.id.txt_history)
    TextView txtHistory;
    @BindView(R.id.txt_settings)
    TextView txtSettings;
    @BindView(R.id.layout_ads_adaptive)
    RelativeLayout layoutAdsAdaptive;

    private FragmentStatePagerAdapter fragmentStatePagerAdapter;
    private final int SPEED_TAB = 0;
    private final int RESULT_TAB = 1;
    private final int SETTING_TAB = 2;
    private int selectTab = 0;
    private int currentTab = 0;
    private Result result;

    int count = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count = 0;
            handler.removeCallbacks(runnable);
        }
    };
    int countResult, countSettings;
    private FirebaseAnalytics firebaseAnalytics;

    private boolean checkProcessTestSucess = true;
    private AdView adView;

    private InterstitialAd mInterstitialAd;
    private boolean checkPermissShowAds;
    private boolean isCheckLoadAds;
    private long timeBegin;
    private long timeEnd;
    private boolean checkTestSuccess;
    private boolean checkSpeedSelected = true;
    private boolean checkSpeedOnly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        countResult = AppPreference.getInstance(this).getKeyShowAds(EXTRA_KEY_ADS_RESULT, 0);
        countSettings = AppPreference.getInstance(this).getKeyShowAds(EXTRA_KEY_ADS_SETTINGS, 0);
        analyticsApplication();
        initAdaptive();

    }

    @Override
    protected void onPause() {
        super.onPause();
        checkPermissShowAds = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkPermissShowAds = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissShowAds = true;
    }

    private void analyticsApplication() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        SpeedFragmentUpdate speedFragment = new SpeedFragmentUpdate();
        ResultsFragment speedResult = new ResultsFragment();
        SettingFragment speedSettings = new SettingFragment();
        if (!isFinishing()) {
            speedFragment.setListener(this);
        }
        fragments.add(speedFragment);
        fragments.add(speedResult);
        fragments.add(speedSettings);

        fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewpager.setOffscreenPageLimit(SETTING_TAB);
        viewpager.setAdapter(fragmentStatePagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkSpeedOnly = true;
                changePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Optional
    @OnClick({R.id.layout_speed, R.id.layout_results, R.id.layout_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_speed:
                changePage(SPEED_TAB);
                sendTabEvent();
                currentTab = SPEED_TAB;
                break;
            case R.id.layout_results:
                resultClick();
                break;
            case R.id.layout_setting:
                settingsClick();
                break;
        }
    }

    @Override
    public void onTestSuccess(Result result) {
//        if (checkProcessTestSucess) {
//            Log.d("DUC","vao test ok");
//            checkProcessTestSucess = false;
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                processTestSuccess(result);
//            }, 1000);
//        }else{
//            Log.d("DUC","vao test not ok");
//        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            processTestSuccess(result);
        }, 800);
    }

    private void processTestSuccess(Result result) {
        Log.d("DUC","c vao 00");
        this.result = result;
        timeBegin = System.currentTimeMillis();
        checkTestSuccess = true;
        if (checkSpeedSelected) {
            Log.d("DUC","c vao 001");
            if (isCheckLoadAds) {
                Log.d("DUC","c vao 11");
                mInterstitialAd.show();
            } else {
                Log.d("DUC","c vao 22");
                successUpdate();
            }
        }
    }

    @Override
    public void callFailOnTestSuccess(Result result) {
        if (checkProcessTestSucess) {
            checkProcessTestSucess = false;
            DataManager.query().getResultDao().save(result);
            processTestSuccess(result);
        }
    }

    @Override
    public void loadFullAdsGoogleClickTest() {
        checkTestSuccess = false;
        checkProcessTestSucess=true;
        loadAdsInterstitial();
        loadInterstitialAd();
    }

    private void resultClick() {
        changePage(RESULT_TAB);
        sendTabEvent();
        currentTab = RESULT_TAB;
    }

    private void settingsClick() {
        changePage(SETTING_TAB);
        sendTabEvent();
        currentTab = SETTING_TAB;
    }

    private void successUpdate() {
        checkTestSuccess = false;
        ((ResultsFragment) fragmentStatePagerAdapter.getItem(RESULT_TAB)).refreshData();
        Gson gson = new Gson();
        String data = gson.toJson(result);
        Intent intent = new Intent(this, CompleteActivity.class);
        intent.putExtra(Constants.DATA, data);
        startActivityForResult(intent, 999);
    }


    private void changePage(int position) {
        selectTab = position;
        switch (position) {
            case SPEED_TAB:
                checkSpeedSelected = true;
                viewpager.setCurrentItem(SPEED_TAB);
                imgSpeed.setImageResource(R.drawable.ic_speed_clicked);
                imgResults.setImageResource(R.drawable.ic_resu);
                imgSetting.setImageResource(R.drawable.ic_setting);

                txtTest.setTextColor(getResources().getColor(R.color.color_blue));
                txtHistory.setTextColor(getResources().getColor(R.color.white));
                txtSettings.setTextColor(getResources().getColor(R.color.white));

                timeEnd = System.currentTimeMillis();
                if (checkTestSuccess && checkSpeedOnly) {
                    checkSpeedOnly = false;
                    if ((timeEnd - timeBegin) > 1000) {
                        successUpdate();
                    } else {
                        if (isCheckLoadAds) {
                            mInterstitialAd.show();
                        } else {
                            successUpdate();
                        }
                    }
                }

                break;

            case RESULT_TAB:
                checkSpeedSelected = false;
                viewpager.setCurrentItem(RESULT_TAB);
                imgSpeed.setImageResource(R.drawable.ic_speed);
                imgResults.setImageResource(R.drawable.ic_resu_clicked);
                imgSetting.setImageResource(R.drawable.ic_setting);

                txtTest.setTextColor(getResources().getColor(R.color.white));
                txtHistory.setTextColor(getResources().getColor(R.color.color_blue));
                txtSettings.setTextColor(getResources().getColor(R.color.white));
                break;

            case SETTING_TAB:
                checkSpeedSelected = false;
                viewpager.setCurrentItem(SETTING_TAB);
                imgSpeed.setImageResource(R.drawable.ic_speed);
                imgResults.setImageResource(R.drawable.ic_resu);
                imgSetting.setImageResource(R.drawable.ic_setting_clicked);

                txtTest.setTextColor(getResources().getColor(R.color.white));
                txtHistory.setTextColor(getResources().getColor(R.color.white));
                txtSettings.setTextColor(getResources().getColor(R.color.color_blue));
                break;
        }
    }

    private void sendTabEvent() {
        int testStatus;
        switch (selectTab) {
            case SPEED_TAB:
                if (currentTab == RESULT_TAB) {
                }
                break;
            case RESULT_TAB:
                testStatus = ((SpeedFragmentUpdate) fragmentStatePagerAdapter.getItem(SPEED_TAB)).getStatus();
                switch (testStatus) {
                    case STATUS_NOT_START:
                        break;
                    case STATUS_DOWNLOAD:
                        break;
                    case STATUS_UPLOAD:
                        break;
                }
                break;
            case SETTING_TAB:
                if (currentTab == SPEED_TAB) {
                    testStatus = ((SpeedFragmentUpdate) fragmentStatePagerAdapter.getItem(SPEED_TAB)).getStatus();
                    switch (testStatus) {
                        case STATUS_NOT_START:
                            break;
                        case STATUS_DOWNLOAD:
                            break;
                        case STATUS_UPLOAD:
                            break;
                    }
                } else {
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initFragment();
    }

    private void initAdaptive() {
        layoutAdsAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(MainActivity.this, getListCrossAdaptive()));
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(MainActivity.this);
        adView.setAdUnitId(getString(R.string.small_banner_screen_main));

        loadBanner();
    }


    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (layoutAdsAdaptive != null) {
                    layoutAdsAdaptive.removeAllViews();
                    layoutAdsAdaptive.addView(adView);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (layoutAdsAdaptive != null) {
                    layoutAdsAdaptive.removeAllViews();
                    layoutAdsAdaptive.addView(UtilAdsCrossAdaptive.getLayoutCross(MainActivity.this, getListCrossAdaptive()));
                }
            }
        });
        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(MainActivity.this, adWidth);
    }

    private void loadAdsInterstitial() {
        Log.d("DUC","load gg full");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_screen_test_success));
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                isCheckLoadAds=false;
                AppPreference.getInstance(MainActivity.this).setKeyShowAds(EXTRA_KEY_ADS_FULL_CLICK_GO, 0);
                successUpdate();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded() && checkPermissShowAds) {
                    isCheckLoadAds = true;
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                isCheckLoadAds = false;
            }
        });

    }

    private void loadInterstitialAd() {
        if (mInterstitialAd != null) {
            AdRequest adRequest = new AdRequest.Builder()
                    .setRequestAgent(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            mInterstitialAd.loadAd(adRequest);
        }
    }
}
