package com.gdt.speedtest.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import com.gdt.speedtest.injection.ApplicationContext;
import com.gdt.speedtest.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();
}
