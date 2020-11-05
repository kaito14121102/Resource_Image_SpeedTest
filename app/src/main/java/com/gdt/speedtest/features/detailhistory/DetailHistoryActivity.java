package com.gdt.speedtest.features.detailhistory;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gdt.speedtest.BuildConfig;
import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.gdt.speedtest.data.manager.DataManager;
import com.gdt.speedtest.database.Result;
import com.gdt.speedtest.features.complete.CompleteActivity;
import com.gdt.speedtest.util.AppPreference;
import com.gdt.speedtest.util.FileUtils;
import com.gdt.speedtest.utilcross.UtilAdsCrossNative;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gdt.speedtest.Constants.EXTRA_ADS_CLICK_RATE_APP;
import static com.gdt.speedtest.Constants.MAX_COUNT_RATE_APP;
import static com.gdt.speedtest.Constants.PRE_REMOVED_ADS;
import static com.gdt.speedtest.Constants.PRE_REMOVED_UNLIMITED_TEST;
import static com.gdt.speedtest.Constants.PRE_UNLIMITED_TEST;
import static com.gdt.speedtest.util.NetworkUtils.getNetworkName;
import static com.gdt.speedtest.utilcross.ListAdsCross.getListCrossNative;

public class DetailHistoryActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 0;
    @BindView(R.id.txt_name_wifi)
    TextView txtNameWifi;
    @BindView(R.id.txt_result_signal)
    TextView txtResultSignal;
    @BindView(R.id.txt_ping)
    TextView txtPing;
    @BindView(R.id.txt_download)
    TextView txtDownload;
    @BindView(R.id.txt_upload)
    TextView txtUpload;
    @BindView(R.id.layout_share_screenshot)
    RelativeLayout layoutShareScreenShot;
    @BindView(R.id.txt_ip_private)
    TextView txtIpPrivate;
    @BindView(R.id.txt_ip_public)
    TextView txtIpPublic;
    @BindView(R.id.layout_ip)
    RelativeLayout layoutIp;
    @BindView(R.id.img_vip)
    ImageView imgVip;
    @BindView(R.id.imvBgInfoSpeed)
    ImageView imvBgInfoSpeed;
    @BindView(R.id.llBgInfo)
    ConstraintLayout llBgInfo;
    @BindView(R.id.imvMeasure)
    ImageView imvMeasure;

    @BindView(R.id.tvTimeImage)
    TextView tvTimeImage;
    @BindView(R.id.tvTimeVideo)
    TextView tvTimeVide;
    @BindView(R.id.txt_end_ping)
    TextView txtEndPing;
    @BindView(R.id.txt_end_download)
    TextView txtEndDownload;
    @BindView(R.id.txt_end_upload)
    TextView txtEndUpload;
    @BindView(R.id.img_ip_address)
    ImageView imgIpAddress;
    @BindView(R.id.layout_ads_native)
    RelativeLayout layoutAdsNative;
    @BindView(R.id.layout_ad_cross)
    RelativeLayout layoutAdCross;

    private Result result;
    private Bitmap bitmap;
    private String networkName;
    private boolean checkShareDone;
    private boolean checkDeleteSuccess;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private Animation animBorder1, animBorder2, animBorder3, animBorder4, animBorder5;
    private Animation animYellow1, animYellow2, animYellow3, animYellow4, animYellow5;

    private AdView mAdView;
    private ViewPager viewPager;
    private DecimalFormat dec;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_history);
        ButterKnife.bind(this);
        share = getSharedPreferences("save_rate", MODE_PRIVATE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent("screen_detail_show", new Bundle());
        networkName = getNetworkName(this);
        dec = new DecimalFormat(Constants.FORMAT);
        getResultFromIntent();
        evaluateSpeedInternet(result.getDownload());
        setTimeGuessDownload(result.getDownload());
        initAdsBanner();
    }

    private void initAdsBanner() {
        layoutAdCross.addView(UtilAdsCrossNative.getInstance().getLayoutCross(DetailHistoryActivity.this, getListCrossNative()));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                layoutAdCross.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
                layoutAdCross.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        //Check show rate app
        checkShowDialogRate();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();

    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void setTimeGuessDownload(Double download) {
        double sizeVideo = 160;
        double sizeImage = 9;

        double timeVideo = sizeVideo/download;
        double timeImage = sizeImage/download;

        tvTimeVide.setText(dec.format(timeVideo) + "s");
        tvTimeImage.setText(dec.format(timeImage) + "s");
    }

    private void evaluateSpeedInternet(Double download) {
        imvMeasure.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean translate = true;
                imvBgInfoSpeed.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                float maxWidth = imvMeasure.getWidth();
                float x1 = 0, x2 = 0, y1 = 0,y2 = 0;

                txtResultSignal.setText(dec.format(download) + "m");
                if(download < 1){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#ff4141"));
                    setColorResult("#ff4141");
                    x1 = 0;
                    x2 = 1;
                    y1 = 0;
                    y2 = 1/6f * maxWidth;
                }else if(download < 2){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#ff8d41"));
                    setColorResult("#ff8d41");
                    x1 = 1;
                    x2 = 2;
                    y1 = 1/6f * maxWidth;
                    y2 = 2/6f * maxWidth;
                }else if(download < 5){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#ffc435"));
                    setColorResult("#ffc435");
                    x1 = 2;
                    x2 = 5;
                    y1 = 2/6f * maxWidth;
                    y2 = 3/6f * maxWidth;
                }else if(download < 10){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#fefb20"));
                    setColorResult("#fefb20");
                    x1 = 5;
                    x2 = 10;
                    y1 = 3/6f * maxWidth;
                    y2 = 4/6f * maxWidth;
                }else if (download < 30){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#1af170"));
                    setColorResult("#1af170");
                    x1 = 10;
                    x2 = 30;
                    y1 = 4/6f * maxWidth;
                    y2 = 5/6f * maxWidth;
                }else if(download < 100){
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#12f9af"));
                    setColorResult("#12f9af");
                    x1 = 30;
                    x2 = 100;
                    y1 = 5/6f * maxWidth;
                    y2 = 6/6f * maxWidth;
                }else {
                    imvBgInfoSpeed.setColorFilter(Color.parseColor("#12f9af"));
                    setColorResult("#12f9af");
                    translate = false;
                    llBgInfo.animate().translationX(maxWidth).setDuration(500).start();
                }

                if(translate){
                    float m = (float) (download - (x2 - x1));
                    float r = ((y2 - y1)/(x2 - x1))*m + y1;
                    llBgInfo.animate().translationX(r).setDuration(500).start();
                }
            }
        });
    }
    private void setColorResult(String color) {
        txtPing.setTextColor(Color.parseColor(color));
        txtDownload.setTextColor(Color.parseColor(color));
        txtUpload.setTextColor(Color.parseColor(color));

        txtEndPing.setTextColor(Color.parseColor(color));
        txtEndDownload.setTextColor(Color.parseColor(color));
        txtEndUpload.setTextColor(Color.parseColor(color));
    }

    private void getResultFromIntent() {
        String stringJson = getIntent().getStringExtra("RESULT");
        Gson gson = new Gson();
        result = gson.fromJson(stringJson, Result.class);
        //set result

        txtPing.setText(dec.format(result.getPing()));
        txtDownload.setText(dec.format(result.getDownload()));
        txtUpload.setText(dec.format(result.getUpload()));

        String[] split = result.getName().split("-_-");
        if (split.length != 0) {
            txtNameWifi.setMaxLines(1);
            txtNameWifi.setEllipsize(TextUtils.TruncateAt.END);
            txtNameWifi.setText("Wi-Fi: "+split[0]);
        }
        if (split.length == 1) {
            txtIpPrivate.setVisibility(View.GONE);
            txtIpPublic.setVisibility(View.GONE);
            imgIpAddress.setVisibility(View.GONE);
            layoutIp.setVisibility(View.GONE);
        } else if (split.length == 2) {
            layoutIp.setVisibility(View.VISIBLE);
            txtIpPrivate.setText("Internal IP: " + split[1]);
        } else if (split.length == 3) {
            layoutIp.setVisibility(View.VISIBLE);
            txtIpPrivate.setVisibility(View.VISIBLE);
            txtIpPublic.setVisibility(View.VISIBLE);
            imgIpAddress.setVisibility(View.VISIBLE);
            txtIpPrivate.setText("Internal IP: " + split[1]);
            txtIpPublic.setText("External IP: " + split[2]);
        }else{
            txtIpPrivate.setVisibility(View.GONE);
            txtIpPublic.setVisibility(View.GONE);
            imgIpAddress.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.img_back, R.id.img_share, R.id.img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_share:
                checkShareDone = true;
                allowPermission();
                break;
            case R.id.img_delete:
                mFirebaseAnalytics.logEvent("click_icon_remove_item", new Bundle());
                checkDeleteSuccess = true;
                dialogAskDelete();
                break;
        }
    }

    private void dialogAskDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    DataManager.query().getResultDao().delete(result);
                    Toast.makeText(DetailHistoryActivity.this, "Delete success", Toast.LENGTH_SHORT).show();

                    if (!isFinishing()) {
                        boolean rate_late = share.getBoolean("rate_late", false);
                        if (!rate_late) {
                            checkShowDialogRate();
                        } else {
                            finish();
                        }
                    }
                } catch (Exception exception) {
                    Toast.makeText(DetailHistoryActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean permissionAllowed = true;
                for (int granted : grantResults) {
                    if (granted != PackageManager.PERMISSION_GRANTED) {
                        permissionAllowed = false;
                        break;
                    }
                }
                if (!permissionAllowed) {
                    Toast.makeText(this, R.string.allow_permission_alert, Toast.LENGTH_LONG).show();
                } else {
                    shareScreenshot();
                }
            }
        }
    }

    private void allowPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                shareScreenshot();
            }
        } else {
            shareScreenshot();
        }
    }

    private void shareScreenshot() {
        layoutShareScreenShot.setDrawingCacheEnabled(true);
        bitmap = layoutShareScreenShot.getDrawingCache();
        String path = FileUtils.saveBitmap(bitmap);
        Intent intent;
        Uri uri;
        File f = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                    DetailHistoryActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider", f);
        } else {
            uri = Uri.fromFile(f.getAbsoluteFile());
        }
        shareImageUri(uri);
    }

    private void shareImageUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/jpg");
        startActivity(intent);
    }

    private void checkShowDialogRate() {
        if (!isFinishing()) {
            boolean rate_late = share.getBoolean("rate_late", false);
            if (!rate_late & checkShareDone) {
                checkShareDone = false;
                CheckRateApp.countRateApp = getCountRateApp();
                CheckRateApp.countRateApp++;
                saveRateApp();

                if (AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_UNLIMITED_TEST, false) ||
                        AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_REMOVED_UNLIMITED_TEST, false)) {
                    if (getCountRateApp() >= 2) {
                        showDialogRateApp(2);
                    }
                } else {
                    if (getCountRateApp() >= MAX_COUNT_RATE_APP) {
                        showDialogRateApp(2);
                    }
                }
            } else if (!rate_late & checkDeleteSuccess) {
                CheckRateApp.countRateApp = getCountRateApp();
                CheckRateApp.countRateApp++;
                saveRateApp();

                if (AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_REMOVED_ADS, false) ||
                        AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_UNLIMITED_TEST, false) ||
                        AppPreference.getInstance(DetailHistoryActivity.this).getKeyRate(PRE_REMOVED_UNLIMITED_TEST, false)) {
                    if (getCountRateApp() >= 2) {
                        showDialogRateApp(2);
                    }else{
                        finish();
                    }
                } else {
                    if (getCountRateApp() >= 3) {
                        showDialogRateApp(2);
                    }else{
                        finish();
                    }
                }
            }
        }
    }

    private void saveRateApp() {
        editor = share.edit();
        editor.putInt(EXTRA_ADS_CLICK_RATE_APP, CheckRateApp.countRateApp);
        editor.commit();
    }

    private int getCountRateApp() {
        return share.getInt(EXTRA_ADS_CLICK_RATE_APP, 0);
    }

    private void showDialogRateApp(final int action) {

        final Dialog dialog1 = new Dialog(this);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_rate_app);
        Typeface type = Typeface.createFromAsset(getAssets(), "r0c0i - Linotte Regular.ttf");
//
        final TextView btnRate = dialog1.findViewById(R.id.btnRate);
        TextView btnLater = dialog1.findViewById(R.id.btnLater);
        final TextView tvTitleDialog = dialog1.findViewById(R.id.tvTitleDialog);
        tvTitleDialog.setTypeface(type);
        tvTitleDialog.setTypeface(type);
        btnLater.setTypeface(type);
        final AtomicInteger Rate = new AtomicInteger();

        animBorder1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);
        animBorder5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_border);

        animYellow1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);
        animYellow5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_stars_yellow);

        //border
        final ImageView imgBorder1 = dialog1.findViewById(R.id.img_stars1);
        final ImageView imgBorder2 = dialog1.findViewById(R.id.img_stars2);
        final ImageView imgBorder3 = dialog1.findViewById(R.id.img_stars3);
        final ImageView imgBorder4 = dialog1.findViewById(R.id.img_stars4);
        final ImageView imgBorder5 = dialog1.findViewById(R.id.img_stars5);

        //yellow
        final ImageView imgStarYellow1 = dialog1.findViewById(R.id.img_stars_1);
        final ImageView imgStarYellow2 = dialog1.findViewById(R.id.img_stars_2);
        final ImageView imgStarYellow3 = dialog1.findViewById(R.id.img_stars_3);
        final ImageView imgStarYellow4 = dialog1.findViewById(R.id.img_stars_4);
        final ImageView imgStarYellow5 = dialog1.findViewById(R.id.img_stars_5);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_border);

        Animation animationY1 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY2 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY3 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);
        Animation animationY4 = AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow);

        animation.setStartOffset(200);
        animation1.setStartOffset(400);
        animation2.setStartOffset(600);
        animation3.setStartOffset(800);

        animationY1.setStartOffset(200);
        animationY2.setStartOffset(400);
        animationY3.setStartOffset(600);
        animationY4.setStartOffset(800);

        imgBorder2.startAnimation(animation);
        imgBorder3.startAnimation(animation1);
        imgBorder4.startAnimation(animation2);
        imgBorder5.startAnimation(animation3);

        imgStarYellow2.startAnimation(animationY1);
        imgStarYellow3.startAnimation(animationY2);
        imgStarYellow4.startAnimation(animationY3);
        imgStarYellow5.startAnimation(animationY4);

        imgBorder1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_stars_border));
        imgStarYellow1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_stars_yellow));


        imgBorder1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.set(1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
            }
        });
        imgBorder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.set(2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
            }
        });
        imgBorder3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.set(3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
            }
        });
        imgBorder4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.set(4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_rounded).into(imgBorder5);
            }
        });
        imgBorder5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rate.set(5);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder1);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder2);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder3);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder4);
                Glide.with(getApplicationContext()).load(R.drawable.ic_star_fill_rounded).into(imgBorder5);
            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRateApp.countRateApp = 0;
                saveRateApp();
                editor = share.edit();
                editor.putBoolean("rate_late", true);
                editor.commit();
                dialog1.dismiss();
                Toast.makeText(DetailHistoryActivity.this, getString(R.string.sms_thank_you_rate), Toast.LENGTH_SHORT).show();
                Intent i = new Intent("android.intent.action.VIEW");
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(i);
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckRateApp.countRateApp = 0;
                saveRateApp();
                if (action == 1) {
                    dialog1.dismiss();
                    animBorder1.cancel();
                    animBorder4.cancel();
                    animBorder2.cancel();
                    animBorder3.cancel();
                    animBorder5.cancel();
                } else if (action == 2) {
                    dialog1.dismiss();
                    editor = share.edit();
                    editor.putBoolean("rate_late", false);
                    editor.commit();
                }
                dialog1.dismiss();
                if(checkDeleteSuccess){
                    finish();
                }
            }
        });

        dialog1.setCancelable(false);
        if (!isFinishing()) {
            dialog1.show();
        }
    }
}
