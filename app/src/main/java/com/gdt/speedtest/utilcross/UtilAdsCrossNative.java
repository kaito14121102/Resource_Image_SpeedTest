package com.gdt.speedtest.utilcross;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gdt.speedtest.Constants.PACKAGE_APPLOCK;
import static com.gdt.speedtest.Constants.PACKAGE_WATERMARK_PHOTO;
import static com.gdt.speedtest.Constants.TYPE_IMAGE;
import static com.gdt.speedtest.utilcross.CheckInstalledApp.appInstalledOrNot;


public class UtilAdsCrossNative {
    @BindView(R.id.img_icon_app)
    ImageView imgIcon;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.screen1)
    ImageView screen1;
    @BindView(R.id.screen2)
    ImageView screen2;
    @BindView(R.id.screen3)
    ImageView screen3;
    @BindView(R.id.txt_description)
    TextView txtDescription;
    @BindView(R.id.img_adchoice)
    ImageView imgAdchoice;
    @BindView(R.id.btn_install)
    Button btnInstall;
    private static UtilAdsCrossNative instance;
    private Context context;
    private CrossItem crossItem;

    public static UtilAdsCrossNative getInstance() {
        if (instance == null) {
            instance = new UtilAdsCrossNative();
        }
        return instance;
    }

    public View getLayoutCross(Context context, ArrayList<CrossItem> listCross) {
        return initView(context, listCross);

    }

    public View initView(Context context, ArrayList<CrossItem> listCross) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ads_cross_splash, null);
        ButterKnife.bind(this, view);
        crossItem = listCross.get(new Random().nextInt(listCross.size()));

        txtName.setMaxLines(2);
        txtName.setEllipsize(TextUtils.TruncateAt.END);
        txtDescription.setMaxLines(2);
        txtDescription.setEllipsize(TextUtils.TruncateAt.END);
        btnInstall.setMaxLines(1);
        btnInstall.setEllipsize(TextUtils.TruncateAt.END);

        txtName.setText(crossItem.getTitle());
        txtDescription.setText(crossItem.getContent());

        //set screenshot
        if (crossItem.getPackagename().equals(PACKAGE_WATERMARK_PHOTO)) {
            //set title, content
            txtName.setText(crossItem.getTitle());
            txtDescription.setText(crossItem.getContent());
            //set icon
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/watermark/ic_icon_watermark" + TYPE_IMAGE))
                    .into(imgIcon);
            //set screenshot
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/watermark/screen_watermark_1" + TYPE_IMAGE))
                    .into(screen1);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/watermark/screen_watermark_2" + TYPE_IMAGE))
                    .into(screen2);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/watermark/screen_watermark_3" + TYPE_IMAGE))
                    .into(screen3);
        }else if (crossItem.getPackagename().equals(PACKAGE_APPLOCK)) {
            //set title, content
            txtName.setText(crossItem.getTitle());
            txtDescription.setText(crossItem.getContent());
            //set icon
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/applock/ic_icon_applock" + TYPE_IMAGE))
                    .into(imgIcon);
            //set screenshot
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/applock/screen_applock_1" + TYPE_IMAGE))
                    .into(screen1);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/applock/screen_applock_2" + TYPE_IMAGE))
                    .into(screen2);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/imgcross/applock/screen_applock_3" + TYPE_IMAGE))
                    .into(screen3);
        }
        if (appInstalledOrNot((Activity) context, crossItem.getPackagename())) {
            btnInstall.setText("INSTALLED");
        } else {
            btnInstall.setText("INSTALL NOW");
        }
        return view;
    }

    @OnClick({R.id.img_icon_app, R.id.txt_name, R.id.screen1, R.id.screen2, R.id.screen3, R.id.txt_description, R.id.btn_install,
            R.id.img_adchoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_icon_app:
            case R.id.txt_name:
            case R.id.screen1:
            case R.id.screen2:
            case R.id.screen3:
            case R.id.txt_description:
            case R.id.btn_install:
                Intent intentUpdate = new Intent(Intent.ACTION_VIEW);
                intentUpdate.setData(Uri.parse(Constants.PLAY_STORE_LINK + crossItem.getPackagename()));
                context.startActivity(intentUpdate);
                break;
            case R.id.img_adchoice:
                Intent intentRate = new Intent(Intent.ACTION_VIEW);
                intentRate.setData(Uri.parse(Constants.URL_INHOUSE_ADS));
                context.startActivity(intentRate);
                break;
        }
    }
}
