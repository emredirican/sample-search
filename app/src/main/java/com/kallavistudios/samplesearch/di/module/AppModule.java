package com.kallavistudios.samplesearch.di.module;

import com.kallavistudios.samplesearch.SampleSearchApplication;
import com.kallavistudios.samplesearch.data.DataManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final SampleSearchApplication application;

    public AppModule(SampleSearchApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public SampleSearchApplication provideApp(){
        return application;
    }

    @Provides
    @Singleton
    public Bus provideBus(){
        return new Bus(ThreadEnforcer.MAIN);
    }

    @Provides
    @Singleton
    public DataManager provideDataManager(){
        return new DataManager(application);
    }
}
