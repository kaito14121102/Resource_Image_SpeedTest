package com.gdt.speedtest.features.main.fragment.result;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdt.speedtest.R;
import com.gdt.speedtest.data.manager.DataManager;
import com.gdt.speedtest.database.Result;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ResultsFragment extends Fragment {
    @BindView(R.id.list_results)
    RecyclerView listResults;

    Unbinder unbinder;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.layoutBannerAds)
    RelativeLayout layoutBannerAds;
    @BindView(R.id.txt_nothing)
    TextView txtNothing;
    private ResultAdapter adapter;
    private boolean checkLoadedAds;
    private boolean checkCreated;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        unbinder = ButterKnife.bind(this, view);
        checkCreated = true;
        checkNothing();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void checkNothing() {
        List<Result> results = DataManager.query().getResultDao().queryBuilder().list();
        if(results.size()>0){
            txtNothing.setVisibility(View.GONE);
        }else{
            txtNothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && checkCreated) {
            checkNothing();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void refreshData() {
        List<Result> results = DataManager.query().getResultDao().queryBuilder().list();
        Collections.reverse(results);
        adapter = new ResultAdapter(getContext(), results);
        if(listResults!=null){
            listResults.setAdapter(adapter);
        }
    }

    private void init() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listResults.setLayoutManager(layoutManager);
        refreshData();
    }
}