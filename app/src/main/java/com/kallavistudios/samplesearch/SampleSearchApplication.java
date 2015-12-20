package com.kallavistudios.samplesearch;

import android.app.Application;

import com.kallavistudios.samplesearch.di.component.SampleSearchComponent;

import timber.log.Timber;


public class SampleSearchApplication extends Application {

    private SampleSearchComponent objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        if (this.objectGraph == null) {
            this.objectGraph = SampleSearchComponent.Initializer.init(this);
        }
        getObjectGraph().inject(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public SampleSearchComponent getObjectGraph() {
        return objectGraph;
    }

    public void setObjectGraph(SampleSearchComponent objectGraph) {
        this.objectGraph = objectGraph;
    }
}
