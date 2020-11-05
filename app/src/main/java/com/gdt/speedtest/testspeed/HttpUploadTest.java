package com.gdt.speedtest.testspeed;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.gdt.speedtest.features.main.fragment.speed.SpeedChangeListener;

import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author erdigurbuz
 */
public class HttpUploadTest extends Thread {
    private int timeOutUpload=9;
    public String fileURL = "";
    static int uploadedKByte = 0;
    double uploadElapsedTime = 0;
    boolean finished = false;
    double elapsedTime = 0;
    double finalUploadRate = 0.0;
    long startTime;
    private Handler handler;
    private boolean endThread = false;
    private Disposable disposable;
    private SpeedChangeListener listener;

    @SuppressLint("HandlerLeak")
    public HttpUploadTest(String fileURL, SpeedChangeListener listener) {
        this.fileURL = fileURL;
        this.listener=listener;
        handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (listener != null) {
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    listener.onUploadFinished();
                }
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
                    if(tick>timeOutUpload){
                        listener.onUploadFinished();
                        if(disposable!=null){
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

    public double getInstantUploadRate() {
        try {
            BigDecimal bd = new BigDecimal(uploadedKByte);
        } catch (Exception ex) {
            return 0.0;
        }

        if (uploadedKByte >= 0) {
            long now = System.currentTimeMillis();
            elapsedTime = (now - startTime) / 1000.0;
            return round((Double) (((uploadedKByte / 1000.0) * 8) / elapsedTime), 2);
        } else {
            return 0.0;
        }
    }

    public double getFinalUploadRate() {
        return round(finalUploadRate, 2);
    }

    @Override
    public void run() {
        try {
            URL url = new URL(fileURL);
            uploadedKByte = 0;
            startTime = System.currentTimeMillis();

            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (int i = 0; i < 4; i++) {
                executor.execute(new HandlerUpload(url));
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
                if (endThread) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    //
                }
            }

            long now = System.currentTimeMillis();
            uploadElapsedTime = (now - startTime) / 1000.0;
            finalUploadRate = (Double) (((uploadedKByte / 1000.0) * 8) / uploadElapsedTime);

        } catch (Exception ex) {
            //
        }

        if (!endThread) {
            handler.sendMessage(handler.obtainMessage());
        }
        finished = true;
    }

    class HandlerUpload extends Thread {

        URL url;

        public HandlerUpload(URL url) {
            this.url = url;
        }

        public void run() {
            byte[] buffer = new byte[150 * 1024];
            long startTime = System.currentTimeMillis();
            int timeout = 8;

            while (true) {

                try {
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());


                    dos.write(buffer, 0, buffer.length);
                    dos.flush();

                    conn.getResponseCode();

                    uploadedKByte += buffer.length / 1024.0;

                    long endTime = System.currentTimeMillis();

                    double uploadElapsedTime = (endTime - startTime) / 1000.0;
                    if (endThread) {
                        break;
                    }
                    if (uploadElapsedTime >= timeout) {
                        break;
                    }

                    dos.close();
                    conn.disconnect();
                } catch (Exception ex) {
                    //
                }
            }
        }
    }
}
