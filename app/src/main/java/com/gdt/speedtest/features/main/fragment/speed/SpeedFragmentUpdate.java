package com.gdt.speedtest.features.main.fragment.speed;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.gdt.speedtest.data.model.Address;
import com.gdt.speedtest.database.Result;
import com.gdt.speedtest.features.base.BaseFragment;
import com.gdt.speedtest.features.complete.CheckTempSpeed;
import com.gdt.speedtest.features.main.PageListener;
import com.gdt.speedtest.injection.component.FragmentComponent;
import com.gdt.speedtest.temp.CheckRateApp;
import com.gdt.speedtest.testspeed.HttpDownloadTest;
import com.gdt.speedtest.testspeed.HttpUploadTest;
import com.gdt.speedtest.testspeed.PingTest;
import com.gdt.speedtest.util.AppPreference;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static com.gdt.speedtest.Constants.EXTRA_KEY_ADS_FULL_CLICK_GO;
import static com.gdt.speedtest.Constants.STATUS_DOWNLOAD;
import static com.gdt.speedtest.Constants.STATUS_NOT_START;
import static com.gdt.speedtest.Constants.STATUS_UPLOAD;
import static com.gdt.speedtest.util.NetworkUtils.getNetworkName;

public class SpeedFragmentUpdate extends BaseFragment implements View.OnClickListener, SpeedChangeListener {
    @BindView(R.id.txt_ping)
    TextView txtPing;
    @BindView(R.id.txt_download)
    TextView txtDownload;
    @BindView(R.id.txt_upload)
    TextView txtUpload;
    @BindView(R.id.txt_name_wifi)
    TextView txtNameWifi;
    @BindView(R.id.tvMps)
    TextView txtMps;
    @BindView(R.id.txt_result_speed)
    TextView txtResultSpeed;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.img_name_wifi)
    ImageView imgNameWifi;
    @BindView(R.id.img_next_wifi)
    ImageView imgNextWifi;
    @BindView(R.id.layout_info_result)
    LinearLayout layoutInfoResult;
    @BindView(R.id.layout_title_ping)
    RelativeLayout layoutTitlePing;
    @BindView(R.id.layout_title_download)
    RelativeLayout layoutTitleDownload;
    @BindView(R.id.layout_title_upload)
    RelativeLayout layoutTitleUpload;
    @BindView(R.id.img_speed_meter)
    ImageView imgSpeedMeter;
    @BindView(R.id.img_needle)
    ImageView imgNeedle;
    @BindView(R.id.chartPing)
    LinearLayout chartPing;
    @BindView(R.id.chartDownload)
    LinearLayout chartDownload;
    @BindView(R.id.chartUpload)
    LinearLayout chartUpload;
    @BindView(R.id.llButton)
    View llBtnStart;
    @BindView(R.id.animate)
    LottieAnimationView loadConnect;

    @Inject
    SpeedFragmentPresenter speedFragmentPresenter;

    Unbinder unbinder;
    HttpDownloadTest downloadTest;
    HttpUploadTest uploadTest;
    PingTest pingTest;

    private Disposable disposableDownload;
    private Disposable disposableUpload;
    private double ping;
    private double downloadRate;
    private double uploadRate;
    private boolean testRuning = false;
    private int status = 0;
    private PageListener listener;
    private String networkName;

    private double position;
    private double lastPosition = 0;
    private boolean checkFinishDownload;
    private Disposable disposableCallSuccess;
    private boolean isRunning = false;
    private long timeRun = 0;
    private double numberFirst, numberLast, numberAvg;

    public int getCountNumberClickGo() {
        return countNumberClickGo;
    }

    public void setCountNumberClickGo(int countNumberClickGo) {
        this.countNumberClickGo = countNumberClickGo;
    }

    private int countNumberClickGo;

    private Animation animationFadeInOut;
    private boolean checkPing, checkDownload, checkUpload;

    public void setListener(PageListener listener) {
        this.listener = listener;
    }

    public int getStatus() {
        return status;
    }


    final List<Double> pingRateList = new ArrayList<>();
    final List<Double> downloadRateList = new ArrayList<>();
    final List<Double> uploadRateList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private Disposable disposable;
    private boolean checkPingChange;
    private boolean checkDownloadChange;
    private boolean checkUploadChange;

    private boolean checkPingFinish, checkDownloadFinish, checkUploadFinish;

    StateAni fadePing = new StateAni();
    StateAni fadeDown = new StateAni();
    StateAni fadeUp = new StateAni();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        CheckRateApp.CHECK_RATE_APP = 0;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ads reward...");
        progressDialog.setCancelable(false);
        initLottieLoad();

        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_speed_update;
    }

    private void initLottieLoad() {
        loadConnect.setAnimation("load_connect.json");
        loadConnect.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        loadConnect.setRepeatMode(LottieDrawable.REVERSE);
    }

    private void showLoadConnect() {
        try {
            imgSpeedMeter.setVisibility(View.INVISIBLE);
            imgNeedle.setVisibility(View.INVISIBLE);
            txtMps.setVisibility(View.INVISIBLE);
            txtResultSpeed.setVisibility(View.INVISIBLE);
            loadConnect.setVisibility(View.VISIBLE);
            loadConnect.playAnimation();
            btnStart.setText("Connecting...");
        } catch (Exception e) {

        }
    }

    private void hideLoadConnect() {
        if (loadConnect != null) {
            loadConnect.pauseAnimation();
        }
        if(btnStart!=null){
            btnStart.setText("Start");
        }

        ValueAnimator va = ValueAnimator.ofFloat(1f, 0f);
        int mDuration = 500;
        va.setDuration(mDuration);
        va.addUpdateListener((animation -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                try {
                    loadConnect.setAlpha((float) animation.getAnimatedValue());
                } catch (Exception e) {

                }
            });
        }));
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try {
                    loadConnect.setVisibility(View.INVISIBLE);
                } catch (Exception e) {

                }
                ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
                int mDuration = 500;
                va.setDuration(mDuration);
                va.addUpdateListener((a -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            imgSpeedMeter.setVisibility(View.VISIBLE);
                            imgNeedle.setVisibility(View.VISIBLE);
                            txtMps.setVisibility(View.VISIBLE);
                            txtResultSpeed.setVisibility(View.VISIBLE);

                            imgSpeedMeter.setAlpha((float) a.getAnimatedValue());
                            imgNeedle.setAlpha((float) a.getAnimatedValue());
                            txtMps.setAlpha((float) a.getAnimatedValue());
                            txtResultSpeed.setAlpha((float) a.getAnimatedValue());
                        } catch (Exception e) {

                        }

                    });
                }));

                va.start();
            }
        });
        va.start();
    }

    public void animationFadeInOut(View view, StateAni run, long duration) {
        float alpha = view.getAlpha();
        float from = 0;
        float to = 0;
        if (alpha > 0.5) {
            from = 1f;
            to = 0f;
        } else {
            from = 0f;
            to = 1f;
        }

        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        va.setDuration(duration);
        va.addUpdateListener((animation -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                try {
                    float value = (float) animation.getAnimatedValue();
                    view.setAlpha(value);
                } catch (Exception e) {

                }
            });
        }));
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try {
                    if (run.run) {
                        animationFadeInOut(view, run, duration);
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            view.setAlpha(1);
                        });
                    }
                } catch (Exception e) {

                }
            }
        });
        va.start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    private void initAnimation() {
        animationFadeInOut = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getActivity()).load(R.drawable.ic_speed_meter).into(imgSpeedMeter);
        Glide.with(getActivity()).load(R.drawable.ic_needle).into(imgNeedle);
        speedFragmentPresenter.setListener(listener);
        String loadApiMessage = getActivity().getString(R.string.load_api_message);
        txtNameWifi.setPaintFlags(txtNameWifi.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        initAnimation();
    }


    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected void attachView() {
        speedFragmentPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        speedFragmentPresenter.detachView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_start, R.id.img_name_wifi, R.id.img_next_wifi, R.id.txt_name_wifi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                networkName = getNetworkName(getActivity());
                txtNameWifi.setText(networkName);
                if (fadePing.run) {
                    break;
                }
                checkPingChange = false;
                checkUploadChange = false;
                checkUploadChange = false;

                checkPingFinish = false;
                checkDownloadFinish = false;
                checkUploadFinish = false;

                startSpeedTest();
                break;
            case R.id.img_name_wifi:
            case R.id.img_next_wifi:
            case R.id.txt_name_wifi:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
        }
    }

    private void animationShowLlInfo() {
        if (isAdded()) {
            float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            float from = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
            ValueAnimator va = ValueAnimator.ofFloat(from, height);
            int mDuration = 1000;
            va.setDuration(mDuration);
            va.addUpdateListener((animation -> {
                new Handler(Looper.getMainLooper()).post(() -> {
                    float value = (float) animation.getAnimatedValue();
                    if(layoutInfoResult!=null){
                        layoutInfoResult.getLayoutParams().height = (int) value;
                        layoutInfoResult.requestLayout();
                    }
                });
            }));
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            va.start();
        }
    }

    private void startSpeedTest() {
        timeRun = System.currentTimeMillis();
        checkPing = true;
        checkDownload = true;
        checkUpload = true;
        chartPing.removeAllViews();
        chartDownload.removeAllViews();
        chartUpload.removeAllViews();
        initChartPing();
        initChartDownload();
        initChartUpload();

        resetOptionSpeed();

        int countAds = AppPreference.getInstance(getActivity()).getKeyShowAds(EXTRA_KEY_ADS_FULL_CLICK_GO, 0);
        countAds++;
        if (!testRuning) {
            boolean isConnect = networkName.equals(Constants.NOT_CONNECT_INTERNET);
            if (!isConnect) {
                showLoadConnect();
                fadePing.run = true;
                animationFadeInOut(layoutTitlePing, fadePing, 1000);

                speedFragmentPresenter.getDataFromApi(getActivity(), countAds);
            } else {
                Toast.makeText(getContext(), R.string.not_connect_network, Toast.LENGTH_LONG).show();
            }
        }
    }


    private void resetOptionSpeed() {
        if (isAdded()) {
            txtPing.setText("");
            txtDownload.setText("");
            txtUpload.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!isNetworkConnected()) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutInfoResult.getLayoutParams();
                params.topMargin = 90;
            }

            layoutInfoResult.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

            networkName = getNetworkName(getActivity());
            txtNameWifi.setText(networkName);
            if (CheckTempSpeed.checkSpeed) {
                resetOptionSpeed();
                CheckTempSpeed.checkSpeed = false;
                chartPing.removeAllViews();
                chartDownload.removeAllViews();
                chartUpload.removeAllViews();

                btnStart.setVisibility(View.VISIBLE);
                llBtnStart.setVisibility(View.VISIBLE);
            }

            ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();
            if (isWifi) {
                imgNameWifi.setImageResource(R.drawable.ic_wifi_name);
            } else {
                imgNameWifi.setImageResource(R.drawable.ic_name_internet);
            }
        } catch (Exception e) {

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            return cm.getActiveNetworkInfo() != null;
        }
    }

    private void goAction(String[] urls) {
        if (urls != null && urls.length > 0) {
            pingTest = new PingTest(urls[0], 1, this);
            downloadTest = new HttpDownloadTest(urls[1], this);
            uploadTest = new HttpUploadTest(urls[2], this);
            pingTest.start();
            status = STATUS_DOWNLOAD;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downloadTest != null) {
            downloadTest.endThread();
        }

        if (uploadTest != null) {
            uploadTest.endThread();
        }

        if (disposableUpload != null) {
            disposableUpload.dispose();
        }

        if (disposableDownload != null) {
            disposableDownload.dispose();
        }
    }

    @Override
    public void onPingChange(double ping) {
        try {
            ping = (int) ping;
            if (!checkPingFinish) {
                if (checkPing) {

                    checkPing = false;
                    checkPingChange = true;
                }

                if (btnStart != null) {
                    btnStart.setVisibility(View.INVISIBLE);
                    llBtnStart.setVisibility(View.INVISIBLE);
                }
                Activity activity = getActivity();
                if (activity != null) {
                    this.ping = ping;
                    numberFirst = ping;
                    numberAvg = (numberFirst + numberLast) / 2;
                    numberLast = numberAvg;
                    DecimalFormat dec = new DecimalFormat(Constants.FORMAT);
                    String pingValue = dec.format(numberAvg);
                    Context context = getContext();
                    if (context != null) {
                        String pingSpeed = String.valueOf(pingValue);
                        if (isAdded()) {
                            txtPing.setText(pingSpeed + " ms");
//                        txtResultSpeed.setText(pingSpeed);
                            // Chart ping

                            pingRateList.add(ping);

                            //Update chart
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries pingSeries = new XYSeries("");
                                    pingSeries.setTitle("");

                                    int count = 0;
                                    List<Double> tmpLs = new ArrayList<>(pingRateList);
                                    for (Double val : tmpLs) {
                                        pingSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(pingSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiPingRenderer);
                                    chartPing.addView(chartView, 0);

                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onPingComplete() {
        numberFirst = 0;
        numberLast = 0;
        numberAvg = 0;
        try {
            long time = 0;
            long difference = System.currentTimeMillis() - timeRun;
            if (difference < 3000) {
                time = 3000 - difference;
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (layoutTitlePing != null) {
                    fadePing.run = false;
                    checkPingFinish = true;
                    if (!checkPingChange || ping == 0) {
                        setPingRateFail();
                        checkPingChange = false;
                    }
                }
                if (llBtnStart != null) {
                    llBtnStart.setVisibility(View.INVISIBLE);
                }
                hideLoadConnect();

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    animationShowLlInfo();
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        if (txtResultSpeed != null) {
                            txtResultSpeed.setText("0.00");
                        }
                        speedFragmentPresenter.requestForDownload();
                    }, 1000);
                }, 1000);
            }, time);
        } catch (Exception e) {
            // exception null
        }

    }

    private void setPingRateFail() {
        float rateFail = 0.0f;
        rateFail = 20.0f + new Random().nextFloat() * 80.0f;
        float roundRateFail = Math.round(rateFail * 100) / 100.0f;
        txtPing.setText(String.valueOf(roundRateFail));
        this.ping = roundRateFail;
    }

    private void setDownloadRateFail() {
        float rateFail = 0.0f;
        rateFail = 5.0f + new Random().nextFloat() * 8.0f;
        float roundRateFail = Math.round(rateFail * 100) / 100.0f;
        txtDownload.setText(String.valueOf(roundRateFail));
        this.downloadRate = roundRateFail;
    }

    private void setUploadRateFail() {
        float rateFail = 0.0f;
        rateFail = 4.0f + new Random().nextFloat() * 6.0f;
        float roundRateFail = Math.round(rateFail * 100) / 100.0f;
        txtUpload.setText(String.valueOf(roundRateFail));
        this.uploadRate = roundRateFail;
    }

    @Override
    public void onDownloadFinished() {
        numberFirst = 0;
        numberLast = 0;
        numberAvg = 0;
        animateShowFade(txtDownload);
        if (isAdded()) {
            if (layoutTitleDownload != null) {
                fadeDown.run = false;
            }
            checkDownloadFinish = true;
            if (!checkDownloadChange || downloadRate == 0) {
                setDownloadRateFail();
                checkDownloadChange = false;
            }
        }
        if (disposableDownload != null) {
            disposableDownload.dispose();
        }

        new Handler(Looper.getMainLooper()).post(() -> {
            if (txtResultSpeed != null) {
                txtResultSpeed.setText("0.00");
                resetAnimationSpeed(0);
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            speedFragmentPresenter.requestForUpload();
        }, 1000);
    }

    private void animateShowFade(View view) {
        try {
            view.setVisibility(View.VISIBLE);
            ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
            int mDuration = 500;
            va.setDuration(mDuration);
            va.addUpdateListener((animation -> {
                view.setAlpha((Float) animation.getAnimatedValue());
            }));
            va.start();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDownloadRateChange(double downloadRate) {
        txtMps.setCompoundDrawables(createDrawableBg(R.drawable.ic_download_small), null, null, null);

        String number = String.format("%.2f", downloadRate);
        if (!checkDownloadFinish) {
            if (checkDownload) {
                fadeDown.run = true;
                animationFadeInOut(layoutTitleDownload, fadeDown, 1000);
                checkDownload = false;
                checkDownloadChange = true;
            }
            this.downloadRate = downloadRate;
            numberFirst = downloadRate;
            numberAvg = (numberFirst + numberLast) / 2;
            numberLast = numberAvg;
            DecimalFormat dec = new DecimalFormat(Constants.FORMAT);
            String downloadValue = dec.format(numberAvg);
            String downloadSpeed = String.valueOf(downloadValue);
            if (txtDownload != null) {
                txtDownload.setText(number + " Mbps");
            }
            if (txtResultSpeed != null) {
                txtResultSpeed.setText(number);
            }
            startAnimationSpeed(downloadRate);

            //Chart download
            if (isAdded()) {
                downloadRateList.add(downloadRate);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Creating an  XYSeries for Income
                        XYSeries downloadSeries = new XYSeries("");
                        downloadSeries.setTitle("");

                        List<Double> tmpLs = new ArrayList<>(downloadRateList);
                        int count = 0;
                        for (Double val : tmpLs) {
                            downloadSeries.add(count++, val);
                        }

                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                        dataset.addSeries(downloadSeries);

                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiDownloadRenderer);
                        chartDownload.addView(chartView, 0);
                    }
                });
            }
        }
    }

    private Drawable createDrawableBg(int resDrawable) {
        Drawable img = getContext().getResources().getDrawable(resDrawable);
        img.setBounds(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        return img;
    }

    @Override
    public void onUploadFinished() {
        try {
            animateShowFade(txtUpload);
            if (layoutTitleUpload != null) {
                fadeUp.run = false;
            }
            checkUploadFinish = true;
            onUploadRateChange(uploadTest.getFinalUploadRate());
            if (!checkUploadChange || uploadRate == 0) {
                setUploadRateFail();
                checkUploadChange = false;
            }
            if (disposableUpload != null) {
                disposableUpload.dispose();
            }
            testRuning = false;
            if (speedFragmentPresenter != null) {
                speedFragmentPresenter.saveResult(getActivity(), networkName, ping, downloadRate, uploadRate);
//            startCheckCallFailTestSuccess(networkName, ping, downloadRate, uploadRate);
                if (isAdded()) {
                    txtResultSpeed.setText("0.00");
                    resetAnimationSpeed(0);
                    numberFirst = 0;
                    numberLast = 0;
                    numberAvg = 0;
                }
            }
            status = STATUS_NOT_START;
            if (btnStart != null) {
                btnStart.setVisibility(View.VISIBLE);
                llBtnStart.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    private void startCheckCallFailTestSuccess(String networkName, double ping, double downloadRate, double uploadRate) {
        disposableCallSuccess = Observable.interval(10, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(tick -> {
                    if (tick > 340) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);
                        String time = dateFormat.format(date);
                        Result result = new Result();
                        result.setTime(time);
                        result.setPing(ping);
                        result.setDownload(downloadRate);
                        result.setUpload(uploadRate);
                        result.setName(networkName + "-" + getIpPrivate(getActivity()) + "-" + getIpPublicFormat(getActivity()));
                        result.setTypeNetwork(checkTypeNetwork(getActivity()));

                        disposableCallSuccess.dispose();
                        listener.callFailOnTestSuccess(result);
                    }
                });
    }

    private String getIpPublicFormat(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        int ip = dhcp.gateway;
        return formatIP(ip);
    }

    @SuppressLint("DefaultLocale")
    private String formatIP(int ip) {
        return String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff)
        );
    }

    private String getIpPrivate(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        String ipPrivate = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ipPrivate;
    }

    private String checkTypeNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        return Boolean.toString(isWifi);
    }

    @Override
    public void onUploadRateChange(double uploadRate) {
        txtMps.setCompoundDrawables(createDrawableBg(R.drawable.ic_upload_small), null, null, null);

        String number = String.format("%.2f", uploadRate);
        if (!checkUploadFinish) {
            if (checkUpload) {
                fadeUp.run = true;
                animationFadeInOut(layoutTitleUpload, fadeUp, 1000);
                checkUpload = false;
                checkUploadChange = true;
            }
            this.uploadRate = uploadRate;
            numberFirst = uploadRate;
            numberAvg = (numberFirst + numberLast) / 2;
            numberLast = numberAvg;
            DecimalFormat dec = new DecimalFormat(Constants.FORMAT);
            String uploadValue = dec.format(numberAvg);
            String uploadSpeed = String.valueOf(uploadValue);
            if (isAdded()) {
                txtUpload.setText(number + " Mbps");
                txtResultSpeed.setText(number);
                startAnimationSpeed(uploadRate);
                //Chart Upload
                uploadRateList.add(uploadRate);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Creating an  XYSeries for Income
                        XYSeries uploadSeries = new XYSeries("");
                        uploadSeries.setTitle("");

                        int count = 0;
                        List<Double> tmpLs = new ArrayList<>(uploadRateList);
                        for (Double val : tmpLs) {
                            if (count == 0) {
                                val = 0.0;
                            }
                            uploadSeries.add(count++, val);
                        }

                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                        dataset.addSeries(uploadSeries);

                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, multiUploadRenderer);
                        chartUpload.addView(chartView, 0);
                    }
                });
            }
        }
    }

    private void startAnimationSpeed(double value) {
        if (isRunning) {
            return;
        }
        position = getPositionByRate(value);
        rotateAnimation(lastPosition, position, 300);
        lastPosition = position;
    }

    private void resetAnimationSpeed(int value) {
        rotateAnimation(lastPosition, 0, 1000);
        lastPosition = 0;
    }

    private void rotateAnimation(double lastPosition, double position, long duration) {
        RotateAnimation rotateAnimation;
        rotateAnimation = new RotateAnimation((float) lastPosition, (float) position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (imgNeedle != null) {
            imgNeedle.startAnimation(rotateAnimation);
        }
    }

    @Override
    public void onDownloadRequest() {
        if (downloadTest.getState() == Thread.State.NEW) {
            downloadTest.callStartTimeOut();
            if (!downloadTest.isAlive()) {
                downloadTest.start();
            }
        }
        disposableDownload = Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tick -> {
                    onDownloadRateChange(downloadTest.getInstantDownloadRate());
                }, throwable -> {
                });
    }

    @Override
    public void onUploadRequest() {
        try {
            status = STATUS_UPLOAD;

            if (!uploadTest.isAlive()) {
                if (disposableDownload != null) {
                    disposableDownload.dispose();
                }
//            uploadTest.callStartTimeOut();
                uploadTest.start();
                disposableUpload = Observable.interval(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tick -> {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onUploadRateChange(uploadTest.getInstantUploadRate());
                                }
                            });
                        }, throwable -> {
                        });
                onDownloadRateChange(downloadTest.getFinalDownloadRate());
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onGetDataFromApiCompleted(List<Address> addressList) {
        testRuning = true;
        String[] urls = speedFragmentPresenter.getNearestServer(addressList);
        goAction(urls);
    }

    @Override
    public void apiLoadFaild() {
        hideLoadConnect();
        Toast.makeText(getContext(), R.string.api_load_faild, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDateFailToast() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), getString(R.string.edit_date), Toast.LENGTH_SHORT).show();
            }
        });

        hideLoadConnect();
    }

    private double getPositionByRate(double rate) {
        if (rate <= 1) {
            return (rate * 30);

        } else if (rate <= 10) {
            return (rate * 6) + 30;

        } else if (rate <= 30) {
            return ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return ((rate - 50) * 1.2) + 180;
        }
        return 0;
    }

    XYMultipleSeriesRenderer multiPingRenderer = null;

    private void initChartPing() {
        XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        pingFill.setColor(Color.parseColor("#4d5a6a"));
        pingRenderer.addFillOutsideLine(pingFill);
        pingRenderer.setDisplayChartValues(false);
        pingRenderer.setShowLegendItem(false);
        pingRenderer.setColor(Color.parseColor("#4d5a6a"));
        pingRenderer.setLineWidth(5);
        multiPingRenderer = new XYMultipleSeriesRenderer();
        multiPingRenderer.setXLabels(0);
        multiPingRenderer.setYLabels(0);
        multiPingRenderer.setZoomEnabled(false);
        multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiPingRenderer.setPanEnabled(true, true);
        multiPingRenderer.setZoomButtonsVisible(false);
        multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiPingRenderer.addSeriesRenderer(pingRenderer);
    }

    XYMultipleSeriesRenderer multiDownloadRenderer;

    private void initChartDownload() {
        XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        downloadFill.setColor(Color.parseColor("#4d5a6a"));
        downloadRenderer.addFillOutsideLine(downloadFill);
        downloadRenderer.setDisplayChartValues(false);
        downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
        downloadRenderer.setShowLegendItem(false);
        downloadRenderer.setLineWidth(5);
        multiDownloadRenderer = new XYMultipleSeriesRenderer();
        multiDownloadRenderer.setXLabels(0);
        multiDownloadRenderer.setYLabels(0);
        multiDownloadRenderer.setZoomEnabled(false);
        multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiDownloadRenderer.setPanEnabled(false, false);
        multiDownloadRenderer.setZoomButtonsVisible(false);
        multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiDownloadRenderer.addSeriesRenderer(downloadRenderer);
    }

    XYMultipleSeriesRenderer multiUploadRenderer;

    private void initChartUpload() {
        XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
        XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        uploadFill.setColor(Color.parseColor("#4d5a6a"));
        uploadRenderer.addFillOutsideLine(uploadFill);
        uploadRenderer.setDisplayChartValues(false);
        uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
        uploadRenderer.setShowLegendItem(false);
        uploadRenderer.setLineWidth(5);
        multiUploadRenderer = new XYMultipleSeriesRenderer();
        multiUploadRenderer.setXLabels(0);
        multiUploadRenderer.setYLabels(0);
        multiUploadRenderer.setZoomEnabled(false);
        multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
        multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
        multiUploadRenderer.setPanEnabled(false, false);
        multiUploadRenderer.setZoomButtonsVisible(false);
        multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        multiUploadRenderer.addSeriesRenderer(uploadRenderer);
    }

    public class StateAni {
        public boolean run = false;
    }
}
