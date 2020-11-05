package com.gdt.speedtest.testspeed;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.gdt.speedtest.features.main.fragment.speed.SpeedChangeListener;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HttpDownloadTest extends Thread {
    private int timeOutDownload = 9;
    private String fileURL = "";
    private long startTime = 0;
    private long endTime = 0;
    private double downloadElapsedTime = 0;
    private int downloadedByte = 0;
    private double finalDownloadRate = 0.0;
    private boolean finished = false;
    private double instantDownloadRate = 0;
    private int timeout = 8;

    private HttpURLConnection httpConn = null;
    private Handler handler;
    private boolean endThread = false;
    private Disposable disposable;
    private SpeedChangeListener listener;

    @SuppressLint("HandlerLeak")
    public HttpDownloadTest(String fileURL, SpeedChangeListener listener) {
        this.fileURL = fileURL;
        this.listener=listener;
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if(disposable!=null){
                    disposable.dispose();
                }
                listener.onDownloadFinished();
            }
        };
    }
    public void callStartTimeOut(){
        if(listener!=null){
            startTimeOut(listener);
        }
    }

    private void startTimeOut(SpeedChangeListener listener) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(tick -> {
                    if (tick > timeOutDownload) {
                        listener.onDownloadFinished();
                        if (disposable != null) {
                            disposable.dispose();
                        }
                    }
                });
    }

    public void endThread() {
        endThread = true;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getInstantDownloadRate() {
        return instantDownloadRate;
    }

    public void setInstantDownloadRate(int downloadedByte, double elapsedTime) {
        if (downloadedByte >= 0) {
            this.instantDownloadRate = round((Double) (((downloadedByte * 8) / (1000 * 1000)) / elapsedTime), 2);
        } else {
            this.instantDownloadRate = 0.0;
        }
    }

    public double getFinalDownloadRate() {
        return round(finalDownloadRate, 2);
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        URL url = null;
        downloadedByte = 0;
        int responseCode = 0;

        List<String> fileUrls = new ArrayList<>();
        fileUrls.add(fileURL + "random4000x4000.jpg");
        fileUrls.add(fileURL + "random3000x3000.jpg");
        fileUrls.add(fileURL + "random2000x2000.jpg");

        startTime = System.currentTimeMillis();

//        outer:
        for (String link : fileUrls) {
            try {
                url = new URL(link);
                httpConn = (HttpURLConnection) url.openConnection();
                responseCode = httpConn.getResponseCode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    byte[] buffer = new byte[1024];

                    InputStream inputStream = httpConn.getInputStream();
                    int len = 0;

                    while ((len = inputStream.read(buffer)) != -1) {
                        downloadedByte += len;
                        endTime = System.currentTimeMillis();
                        downloadElapsedTime = (endTime - startTime) / 1000.0;
                        setInstantDownloadRate(downloadedByte, downloadElapsedTime);
                        if (endThread) {
                            break;
                        }
                        if (downloadElapsedTime >= timeout) {
                            break;
                        }
                    }

                    inputStream.close();
                    httpConn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        endTime = System.currentTimeMillis();
        downloadElapsedTime = (endTime - startTime) / 1000.0;
        finalDownloadRate = ((downloadedByte * 8) / (1000 * 1000.0)) / downloadElapsedTime;

        if (!endThread) {
            handler.sendMessage(handler.obtainMessage());
        }
        finished = true;
    }
}

