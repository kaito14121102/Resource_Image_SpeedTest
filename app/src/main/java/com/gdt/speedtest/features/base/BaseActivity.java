package com.gdt.speedtest.features.base;

import android.os.Bundle;
import androidx.collection.LongSparseArray;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.gdt.speedtest.SpeedTestApplication;
import com.gdt.speedtest.injection.component.ActivityComponent;
import com.gdt.speedtest.injection.component.ConfigPersistentComponent;
import com.gdt.speedtest.injection.component.DaggerConfigPersistentComponent;
import com.gdt.speedtest.injection.module.ActivityModule;
import com.gdt.speedtest.util.ViewUtil;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent> componentsArray =
            new LongSparseArray<>();

    private long activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        activityId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_ACTIVITY_ID)
                        : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (componentsArray.get(activityId) == null) {
            configPersistentComponent =
                    DaggerConfigPersistentComponent.builder()
                            .appComponent(SpeedTestApplication.get(this).getComponent())
                            .build();
            componentsArray.put(activityId, configPersistentComponent);
        } else {
            configPersistentComponent = componentsArray.get(activityId);
        }
        ActivityComponent activityComponent =
                configPersistentComponent.activityComponent(new ActivityModule(this));
        inject(activityComponent);
        ViewUtil.transparentStatus(this);
        attachView();
    }

    protected abstract int getLayout();

    protected abstract void inject(ActivityComponent activityComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            componentsArray.remove(activityId);
        }
        detachPresenter();
        super.onDestroy();
    }
}
