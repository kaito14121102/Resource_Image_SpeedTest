package com.gdt.speedtest.testspeed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gdt.speedtest.Constants;
import com.gdt.speedtest.features.main.fragment.speed.SpeedChangeListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PingTest extends Thread {
    private int timeout=9;
    String server = "";
    int count;
    double instantRtt = 0;
    double avgRtt = 0.0;
    boolean finished = false;
    private Handler handler;
    private Disposable disposable;

    @SuppressLint("HandlerLeak")
    public PingTest(String serverIpAddress, int pingTryCount, SpeedChangeListener listener) {
        this.server = serverIpAddress;
        this.count = pingTryCount;
        handler=new Handler(){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Bundle bundle=message.getData();
                boolean complete=bundle.getBoolean(Constants.COMPLETE_KEY, false);
                if (complete){
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    listener.onPingComplete();
                } else {
                    double pingSpeed = bundle.getDouble(Constants.PING_KEY);
                    listener.onPingChange(pingSpeed);
                }
            }
        };
        startTimeOut(listener);
    }

    private void startTimeOut(SpeedChangeListener listener) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(tick -> {
                    if(tick>timeout){
                        listener.onPingComplete();
                        disposable.dispose();
                    }
                });
    }

    public double getAvgRtt() {
        return avgRtt;
    }

    public double getInstantRtt() {
        return instantRtt;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("ping", "-c " + count, this.server);

            ps.redirectErrorStream(true);
            Process pr = ps.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("icmp_seq")) {
                    instantRtt = Double.parseDouble(line.split(" ")[line.split(" ").length - 2].replace("time=", ""));
                    Bundle bundle=new Bundle();
                    bundle.putDouble(Constants.PING_KEY, instantRtt);
                    Message message=new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                if (line.startsWith("rtt ")) {
                    avgRtt = Double.parseDouble(line.split("/")[4]);
                    Bundle bundle=new Bundle();
                    bundle.putDouble(Constants.PING_KEY, avgRtt);
                    Message message=new Message();
                    message.setData(bundle);
                    handler.sendMessage(message);
                    break;
                }
            }
            pr.waitFor();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle=new Bundle();
        bundle.putBoolean(Constants.COMPLETE_KEY, true);
        Message message=new Message();
        message.setData(bundle);
        handler.sendMessage(message);
        finished = true;
    }

}
