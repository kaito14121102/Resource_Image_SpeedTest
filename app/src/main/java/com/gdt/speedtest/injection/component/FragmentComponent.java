package com.gdt.speedtest.injection.component;

import com.gdt.speedtest.features.main.fragment.result.ResultsFragment;
import com.gdt.speedtest.features.main.fragment.settings.SettingFragment;
import com.gdt.speedtest.features.main.fragment.speed.SpeedFragmentUpdate;
import com.gdt.speedtest.injection.PerFragment;
import com.gdt.speedtest.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(ResultsFragment resultsFragment);
    void inject(SettingFragment settingFragment);
    void inject(SpeedFragmentUpdate speedFragment);
}
