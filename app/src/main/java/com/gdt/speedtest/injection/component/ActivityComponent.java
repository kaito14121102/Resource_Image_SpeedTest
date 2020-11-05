package com.gdt.speedtest.injection.component;

import com.gdt.speedtest.features.main.MainActivity;
import com.gdt.speedtest.features.complete.CompleteActivity;
import com.gdt.speedtest.injection.PerActivity;
import com.gdt.speedtest.injection.module.ActivityModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActitity);
    void inject(CompleteActivity completeActivity);
}
