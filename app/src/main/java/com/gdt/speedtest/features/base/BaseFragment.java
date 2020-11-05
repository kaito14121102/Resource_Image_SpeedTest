package com.gdt.speedtest.features.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.collection.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gdt.speedtest.SpeedTestApplication;
import com.gdt.speedtest.injection.component.ConfigPersistentComponent;
import com.gdt.speedtest.injection.component.DaggerConfigPersistentComponent;
import com.gdt.speedtest.injection.component.FragmentComponent;
import com.gdt.speedtest.injection.module.FragmentModule;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;

/**
 * Abstract Fragment that every other Fragment in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent are kept
 * across configuration changes.
 */
public abstract class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
            new LongSparseArray<>();
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private long fragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        fragmentId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_FRAGMENT_ID)
                        : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent = null;
        if (componentsArray.get(fragmentId) == null) {
            Activity activity = getActivity();
            if (activity != null) {
                configPersistentComponent =
                        DaggerConfigPersistentComponent.builder()
                                .appComponent(SpeedTestApplication.get(activity).getComponent())
                                .build();
                componentsArray.put(fragmentId, configPersistentComponent);
            }

        } else {
            configPersistentComponent = componentsArray.get(fragmentId);
        }
        FragmentComponent fragmentComponent =
                configPersistentComponent.fragmentComponent(new FragmentModule(this));
        inject(fragmentComponent);
        attachView();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayout();

    protected abstract void inject(FragmentComponent fragmentComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, fragmentId);
    }

    @Override
    public void onDestroy() {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            if (activity.isChangingConfigurations()) {
                componentsArray.remove(fragmentId);
            }
        }

        detachPresenter();
        super.onDestroy();
    }
}
