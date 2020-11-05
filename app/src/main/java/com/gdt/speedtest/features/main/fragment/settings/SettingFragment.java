package com.gdt.speedtest.features.main.fragment.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdt.speedtest.Constants;
import com.gdt.speedtest.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.gdt.speedtest.Constants.POLICY_LINK;


public class SettingFragment extends Fragment {
    Unbinder unbinder;

    @OnClick({R.id.layout_rate_app, R.id.layout_share_app, R.id.layout_privacy,R.id.layout_report_bug})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_rate_app:
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intentRate = new Intent(Intent.ACTION_VIEW);
                    intentRate.setData(Uri.parse(Constants.PLAY_STORE_LINK + getContext().getPackageName()));
                    startActivity(intentRate);
                }, 100);
                break;
            case R.id.layout_share_app:
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, Constants.PLAY_STORE_LINK + getContext().getPackageName());
                    sendIntent.setType(Constants.DATA_TYPE);
                    startActivity(sendIntent);
                }, 100);
                break;
            case R.id.layout_privacy:
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intentPolicy = new Intent(Intent.ACTION_VIEW);
                    intentPolicy.setData(Uri.parse(POLICY_LINK));
                    startActivity(intentPolicy);
                }, 100);

                break;
            case R.id.layout_report_bug:
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    String mailSubject = getString(R.string.mail_subject);
                    String mailContent = getString(R.string.mail_content);
                    Intent mailIntent = new Intent(Intent.ACTION_SEND);
                    mailIntent.setType(Constants.MAIL_TYPE);
                    mailIntent.putExtra(Intent.EXTRA_EMAIL, Constants.MAIL_LIST);
                    mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                    mailIntent.putExtra(Intent.EXTRA_TEXT, mailContent);
                    startActivity(Intent.createChooser(mailIntent, mailSubject + ":"));
                }, 100);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
